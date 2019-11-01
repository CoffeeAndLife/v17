$(document).ready(function() {
	/*点击显示二维码 */
	$(".erweima").click(function(event) {
		$(".ewm-tu").toggle();
	});

	/*切换城市 click 事件*/
	$(".dianji-qh").click(function(event) {
		$(".select-city").show();
	});
	$(".close-select-city").click(function(event) {
		$(".select-city").hide();
	});
	$(".sl-city-con dd a").click(function(event) {
		var dqm = $(this).text();
		$(".dqm").text(dqm);
		$(".select-city").hide();
	});

	/*商品分类快捷 click 事件*/
	$("[kj]").click(function(event) {
		$("[kj-sh]").toggle();
	});
	
	$("[mg]").hover(function(){
		$(this).addClass("hover-show-bg");
		var  a1 = $(this).attr("mg");
		$("[mg2="+a1+"]").show();
	},function(){
		$(this).removeClass("hover-show-bg");
		var  a1 = $(this).attr("mg");
		$("[mg2="+a1+"]").hide();
	});

	/*search 切换*/
	$("[ss-search]").click(function(event) {
		$(this).addClass('current').siblings().removeClass("current");
		var sh = $(this).attr("ss-search");
		$("[ss-search-show="+sh+"]").show().siblings().hide();
	});

//轮播图
	var tu=0;
	var spend=1000;
	/*点击  右侧*/
	$(".bnr-right").click(function(){
		dsq();
	});
	/*点击  左侧*/
	$(".bnr-left").click(function(){
		tu--;
		if(tu<0){
			tu=4;
			$(".banner ul").css("left",-6000);
		}
		$(".banner ul").animate({"left":-tu*1200},spend);
	});
	/*定时器 开启*/
	var time=setInterval(dsq,2000);
	function dsq(){
		tu++;
		if(tu>5){
			tu=1;
			$(".banner ul").css("left",0);
		}
		$(".banner ul").animate({"left":-tu*1200},spend);
	}
	$(".banner").hover(function(){
		clearInterval(time);	},function(){
		clearInterval(time);
		time=setInterval(dsq,2000);
	});
//轮播图 END

	$("[hover]").hover(function() {
		var name = $(this).attr("hover");
		$("[hover-after="+name+"]").show();
	}, function() {
		var name = $(this).attr("hover");
		$("[hover-after="+name+"]").stop().hide();
	});
	
	/*商家店铺页面 js 开始*/
	$("[click]").click(function(){
		var ck = $(this).attr("click");
		if (ck) {
			$("[click-chage="+ck+"]").slideToggle(500);
		}
	});

	/*个人资料（上传头像）*/
	$("[shangchuang]").click(function(event) {
		$(".tanchuang").show();
	});
	$("[delete]").click(function(event) {
		$(".tanchuang").hide();
	});
	
	/*我的订单(移入收藏夹)*/
	$(".favorites .fa-left li").hover(function() {
		$(this).addClass('current').siblings().removeClass('current');
		$(".fa-right .fa-r-con .fa-con-uls").eq($(this).index()).show().siblings().hide();
	});
	
	$("[uls1] li").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
		var us1 = $(this).parent("[uls1]").attr("uls1");
		$("[uls2="+us1+"] .ul1").eq($(this).index()).show().siblings().hide();
	});

	/*我的订单(查看物流)*/
	$("[ckwl]").click(function(){
		$(".view-logistics").show();
	});
	$("[guanbi]").click(function(){
		$(".view-logistics").hide();
	});
	
	/*购物车 js*/
	$(".info-mid .mid-guige").hover(function() {
		$(this).addClass('mid-guige2');
	}, function() {
		$(this).removeClass('mid-guige2');
	});
		/*规格弹窗*/
	$("[xg]").click(function(event) {
		var th = $(this).attr("xg");
		$("[xg-g="+th+"]").show();
	});
	$("[qx]").click(function(event) {
		var th = $(this).attr("qx");
		$("[xg-g="+th+"]").hide();
	});
	$(".xg-left dd a").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
	});
		/*数量加减*/
	$(".mid-sl .sl-right").click(function(event) {
		var vl = $(this).siblings("input").val();
		if (vl>0) {
			vl++;
			$(this).siblings("input").val(vl);
		}
	});
	$(".mid-sl .sl-left").click(function(event) {
		var vl = $(this).siblings("input").val();
		if (vl>1) {
			vl--;
			$(this).siblings("input").val(vl);
		}
	});

	/*确认订单（新增地址） js*/
	$(".pay-xdz-btn").click(function(event) {
		$("[xgdz1]").show();
	});
	$("[dz-guan]").click(function(event) {
		$("[xgdz1]").hide();
	});
		/*修改地址*/
	$("[xiugai]").click(function(event) {
		$("[xgdz2]").show();
		var dz1 = $(this).siblings("h3").children('.sp1').text();  /*重庆*/
		var dz2 = $(this).siblings("h3").children('.sp2').text();  /*巴南区*/
		var dz3 = $(this).siblings("h3").children('.sp3').text();  /*赵珍珍*/
		var dz4 = $(this).siblings("p").children('.sp1').text();  /*详细地址*/
		var dz5 = $(this).siblings("p").children('.sp2').text();  /*电话*/
		$("[xgdz2] .dz-left p").text(dz1);
		$("[xgdz2] .dz-right p").text(dz2);
		$("[xgdz2] .tc-con2 li .textarea1").val(dz4);
		$("[xgdz2] .tc-con2 li .shxm").val(dz3);
		$("[xgdz2] .tc-con2 li .lxdh").val(dz5);
	});
	$("[dz-guan]").click(function(event) {
		$("[xgdz2]").hide();
	});

		/*左边 js*/
	$(".dz-left p").click(function(event) {
		$(".dz-left ul").show();
	});
	$(".dz-left li").click(function(event) {
		$(this).addClass('current').siblings().removeClass("current");
		$(".dz-left p").text($(this).text());
		$(".dz-left ul").hide();
	});
		/*右边 js*/
	$(".dz-right p").click(function(event) {
		$(".dz-right ul").show();
	});
	$(".dz-right li").click(function(event) {
		$(this).addClass('current').siblings().removeClass("current");
		$(".dz-right p").text($(this).text());
		$(".dz-right ul").hide();
	});

		/*确认订单 地址切换*/
	$(".pay-dz li").click(function(event) {
		$(this).addClass('current').siblings().removeClass("current");
	});

	/*商品详情 js*/
	$("[qie-xiao] li").hover(function() {
		$(this).addClass('current').siblings().removeClass('current');
		$("[qie-da] li").eq($(this).index()).show().siblings().hide();
	});

	var dtu=0;
	var xtu=0;
	var spend=1000;
	$(".dt-qie-right").click(function(event) {
		xtu++;
		if(xtu>3){
			xtu=0;
			$("[qie-xiao]").css("left",0);
		}
		$("[qie-xiao]").animate({"left":-xtu*72},spend);
	});
	$(".dt-qie-left").click(function(event) {
		xtu--;
		if(xtu<0){
			xtu=3;
			$("[qie-xiao]").css("left",-72);
		}
		$("[qie-xiao]").animate({"left":-xtu*72},spend);
	});
		/*内容-p-中间*/
	$(".dt-if-dd1 a").click(function(event) {
		$(this).addClass('current').siblings().removeClass("current");
	});

			/*内容切换*/
	$("[com-det]").click(function(event) {
		var dt = $(this).attr("com-det");
		$(this).addClass('current').siblings().removeClass("current");
		$("[com-det-show]").hide();
		$("[com-det-show="+dt+"]").show();
	});
			/*dt-if1-m*/
	$(".dt-ifm-box3 .box3-right").click(function(event) {
		var val = $(".dt-ifm-box3 input").val();
		if(val>0){
			val++;
		}
		$(".dt-ifm-box3 input").val(val);
	});
	$(".dt-ifm-box3 .box3-left").click(function(event) {
		var val = $(".dt-ifm-box3 input").val();
		if(val>1){
			val--;
		}
		$(".dt-ifm-box3 input").val(val);
	});

	/*滚动栏*/
	$("[dw]").hover(function() {
		var w = $(this).attr("dw");
		$("[dw-show="+w+"]").show();
	}, function() {
		var w = $(this).attr("dw");
		$("[dw-show="+w+"]").hide();
	});

	/*商品详情(酒店) js*/
	$("[chakan]").click(function(event) {
		var ck = $(this).attr("chakan");
		$("[chakan-tg="+ck+"]").slideToggle(500);
	});

	/*美容美发摄影首页  js*/
	$("[at]").click(function(event) {
		var ap = $(this).parents().siblings("[ap]").text();
		if(ap>0){
			ap++;
		}
		$(this).parents().siblings("[ap]").text(ap);
	});
	$("[ab]").click(function(event) {
		var ap = $(this).parents().siblings("[ap]").text();
		if(ap>1){
			ap--;
		}
		$(this).parents().siblings("[ap]").text(ap);
	});

	$(".img1").change(function(){
		if($(this).val()){
			var objUrl = gebjectURL(this.files[0]) ;
			console.log("objUrl = "+objUrl) ;
			var objUrl = gebjectURL(this.files[0]) ;
			$(this).prev("img").attr("src",objUrl)
		}
	})
	
	function gebjectURL(file) {
		var url = null ; 
		if (window.createObjectURL!=undefined) { // basic
			url = window.createObjectURL(file) ;
		} else if (window.URL!=undefined) { // mozilla(firefox)
			url = window.URL.createObjectURL(file) ;
		} else if (window.webkitURL!=undefined) { // webkit or chrome
			url = window.webkitURL.createObjectURL(file) ;
		}
		return url ;
	}

	/*会员积分 js*/
	$(".mem-info2 li").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
		var jfhq = $(this).attr("jfhq");
		if(jfhq=="qb"){
			$("[hpjf]").show();
		}else{
			$("[hpjf="+jfhq+"]").show().siblings().hide();
		}
	});

	/*积分商城*/
	$(".integral-left li").click(function(event) {
		$(this).addClass('int-current').siblings().removeClass('int-current');
		var intli = $(this).index();
		$(".int-r-con .int-con-ul1").eq(intli).show().siblings(".int-con-ul1").hide();
	});
	/*商城活动*/
	$(".integral-left li").click(function(event) {
		$(this).addClass('int-current').siblings().removeClass('int-current');
		var intli = $(this).index();
		$(".int-r-con .int-con-ul2").eq(intli).show().siblings(".int-con-ul2").hide();
	});

	/*收藏的店铺 js*/
	$("[shuxing1] li").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
		$("[shuxingsw1] ul").eq($(this).index()).show().siblings().hide();
	});
	$("[shuxing2] li").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
		$("[shuxingsw2] ul").eq($(this).index()).show().siblings().hide();
	});
	$("[shuxing3] li").click(function(event) {
		$(this).addClass('current').siblings().removeClass('current');
		$("[shuxingsw3] ul").eq($(this).index()).show().siblings().hide();
	});
	/*搜索列表页(有品牌) js*/
	/*$("[ppgd]").click(function(event) {
		var ht = $(".hover-brand ul").css("height");
		if(ht=="106px"){
			$(".hover-brand ul").css('height','212');
		}else if(ht!="106px"){
			$(".hover-brand ul").css('height','106');
		}
	});*/
	$("[ppgd]").click(function(event) {
		$(".hover-brand").hide();
		$(".re-brand").show();
	});
	$("[quxiao1]").click(function(event) {
		$(".hover-brand").show();
		$(".re-brand").hide();
	});
});











