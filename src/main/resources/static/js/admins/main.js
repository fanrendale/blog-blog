/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {


	// 菜单事件
	//筛选为类blog-menu下面的类list-group-item，顺序不能颠倒
	$(".blog-menu .list-group-item").click(function() {

        var url = $(this).attr("url");

		//首先移除菜单所有的点击样式，然后再将当前点击的添加上
		$(".blog-menu .list-group-item").removeClass("active");
		$(this).addClass("active");

		//加载相应的url对应的页面到右侧页面
		$.ajax({
			url:url,
			type:"GET",
			success:function (data) {
				$("#rightContainer").html(data);
            },
			error:function (data) {
				alert("error");
            }
		});
	});
	
	//默认选中菜单的第一项
	$(".blog-menu .list-group-item:first").trigger("click");
});