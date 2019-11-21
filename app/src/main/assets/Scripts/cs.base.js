var csInited = false;

//Global function
function csWait(cfg) {
    cfg.times = 0;
    var id = setInterval(function() {
        cfg.times++;
        if (cfg.times > 30) {
            clearInterval(id);
        }
        var valid = cfg.when();
        if (valid) {
            clearInterval(id);
            cfg.work();
        }
    }, 100);
}
function isNullOrEmpty(s) {
    if (!s)
        return true;
    if (s == null)
        return true;
    if (s == "")
        return true;
    return false;
}
function goback() {
    $.cs.goBack();
}
function getConfig(k, v) {
    var o = local.getConfig(k);
    if(o == ''){
        if(v){
            return v;
        }
    }
    return o;
}
function saveConfig(k, v) {
    local.saveConfig(k, v);
}

//Cs object
$.cs = {};
$.extend($.cs, {
	parseJson: function(str){
		return eval('(' + str + ')');
	},
    stringify: function(obj) {
        if (window.JSON) {
            return JSON.stringify(obj);
        }
    },
    newGuid: function() {
        var g = "";
        var i = 32;
        while (i--) {
            g += Math.floor(Math.random() * 16.0).toString(16);
        }
        return g;
    },

	emptyPage: function(page){
    	page.find('.cs_navcontainer').each(function(){
			var self = $(this);
			$.cs.historyStack[self.attr('id')] = [];
		});
    },

    removePage: function(page){
    	$.cs.emptyPage(page);
		page.remove();
    },

    topContainerId: 'global-content',
    currentNavContainerId: null,
    historyStack: [],
    gotoPage: function(o) {
		if(o.popPreviousPage){
			var containerId = null;
			if(o.containerId){
				containerId = o.containerId;
			}else{
				containerId = $.cs.topContainerId;
			}
			var history = $.cs.historyStack[containerId];
			if(history.length > 0){
				var p = history.pop();
				$.cs.removePage($('#' + p.pageId));
			}
		}
		var html = '';
		if (o.source == 'asset') {
			html = local.readAssetFile(o.url);
			$.cs.addPage({containerId: o.containerId, url: o.url, html: html});
		}
	},
    addPage: function(o) {
        var containerId = null;
        if(o.containerId){
            containerId = o.containerId;
        }else{
            containerId = $.cs.topContainerId;
        }
        var page = $('<div></div>');
		if(containerId == $.cs.topContainerId){
			page.addClass('fullfill');
		}

        var pageId = $.cs.newGuid();
        page.attr('id', pageId);
        page.attr('url', o.url);
        var history = $.cs.historyStack[containerId];
        if(history.length > 0){
            var lastPage = $('#' + history[history.length - 1].pageId);
            lastPage.removeClass('active');
            lastPage.hide();
        }
        page.addClass('active');
        history.push({ type: 'page', pageId: pageId });
        $.cs.currentNavContainerId = containerId;
        $('#' + containerId).append(page);
        page.html(o.html);
        $.cs.processJsForPage(page);
    },
    goBack: function(o) {
        var containerId = $.cs.currentNavContainerId;
        var history = $.cs.historyStack[containerId];
        while(containerId != $.cs.topContainerId && history.length < 2){
            containerId = $('#' + containerId).parents('.cs_navcontainer')[0].id;
            history = $.cs.historyStack[containerId];
        }
        $.cs.currentNavContainerId = containerId;
        var canBack = false;
        if(history.length > 1){
            canBack = true;
        }
        if (canBack) {
            var p = history.pop();
            var page = $('#' + p.pageId);
            $.cs.removePage(page);

            p = history[history.length - 1];
            page = $('#' + p.pageId);
            page.show();
            page.addClass('active');

            if(o && o.refresh){
            	var refreshTriggerEle = page.find('.refreshtrigger');
                if(refreshTriggerEle.length > 0){
                	if(o.refreshPara){
                		refreshTriggerEle.data('refreshPara', o.refreshPara);
                	}
                    refreshTriggerEle.trigger('click');
                }
            }
        } else {
            local.exit();
        }
    },

    processJsForPage: function(pageEle){
        $.cs.bindPageEvents(pageEle);
        //Process other UI effects, 'tab', validator, dropdown, etc.
    },

    bindPageEvents: function(pageEle) {
        pageEle.find('.assetpagelink').click(function() {
            var self = $(this);
            $.cs.gotoPage({ containerId: self.closest('.cs_navcontainer').attr('id'), source: 'asset', url: self.data('url') });
        });

        pageEle.find('.cs_navcontainer').each(function(){
            var self = $(this);
            if(isNullOrEmpty(self.attr('id'))){
                self.attr('id', $.cs.newGuid());
            }
            $.cs.historyStack[self.attr('id')] = [];
			if(self.data('initurl')){
        		$.cs.gotoPage({url: self.data('initurl'), containerId: self.attr('id')});
        	}
        });
    },

    init: function() {
        $.cs.bindPageEvents($('body'));
        $.cs.currentNavContainerId = $.cs.topContainerId;

        csInited = true;
    }
});

function loadAfterCsInited(callBack) {
    $.cs.init();
    csWait({
        when: function() {
            return csInited;
        },
        work: function() {
            callBack();
        }
    });
}
$(function() {
    loadAfterCsInited(pageload);
});