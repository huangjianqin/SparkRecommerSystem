$(function() {
	var _w = 456;
	var _h = 344;
	var _old = {};
	var _new = {};
	var _txt = $(".textarea textarea");

	$(".upload-btn input").on("change", function() {
		var _this = $(this);
		var fr = new FileReader();
		fr.readAsDataURL(this.files[0]);

		var img = new Image();
		var btn = _this.parent();
		btn.hide();
		var upImg = btn.siblings(".upload-img");
		upImg.addClass("loading");

		fr.onload = function() {
			img.src = this.result;
			img.onload = function() {
				btn.siblings(".upload-img").html(img);
				var ratio = 1;
				if (img.width > img.height) {
					upImg.find("img").addClass("mh");
					ratio = _h / img.height;
				} else {
					upImg.find("img").addClass("mw");
					ratio = _w / img.width;
				}

				var scroll = new IScroll(upImg[0], {
					zoom : true,
					scrollX : true,
					scrollY : true,
					mouseWheel : true,
					bounce : false,
					wheelAction : 'zoom'
				});

				if (btn.hasClass("btn-old")) {
				//	ajaxFileUpload("image_btn1", "#image1");
					_old.img = img;
					_old.scroll = scroll;
					_old.ratio = ratio;
				}
				if (btn.hasClass("btn-new")) {
					//ajaxFileUpload("image_btn2", "#image2");
					_new.img = img;
					_new.scroll = scroll;
					_new.ratio = ratio;
				}

				setTimeout(function() {
					upImg.removeClass("loading").find("img").css("opacity", 1);
				}, 1000);
			}
		}
	});

	$(".submit").on(
			"click",
			function() {
				var jsonResult = {
					"image1" : $("#image1").val(),
					"image2" : $("#image2").val(),
					"text3" : $("#text3").val(),
					"template_id":$("#template_id").val(),
					"emp_no":$("#emp_no").val()
				};
				
				$.ajax({
					  type: 'POST',
					  url: '/xdb/web/sharing!sharing.do',
					  data: {"jsonStr" : JSON.stringify(jsonResult)},
					  success: function(date){
						  alert(date.msg);
						  window.location.href="/xdb/web/sharing!sharingDetail.do?sharingId=".concat(date.msg);
					  },
					  dataType: "json"
					});
				return;
				if (!_old.img) {
					alert("请上传以前照片");
					return false;
				}
				if (!_new.img) {
					alert("请上传现在照片");
					return false;
				}
				if ($.trim(_txt.val()) == "") {
					alert("请输入描述");
					return false;
				}

				var oldImg = imageData(_old);
				var newImg = imageData(_new);

				alert(oldImg.substring(0, 50));
				alert(newImg.substring(0, 50));
			});

	function imageData(obj) {
		obj.scroll.enabled = false;
		var canvas = document.createElement('canvas');

		canvas.width = _w;
		canvas.height = _h;
		var ctx = canvas.getContext('2d');

		var w = _w / obj.scroll.scale / obj.ratio;
		var h = _h / obj.scroll.scale / obj.ratio;
		var x = Math.abs(obj.scroll.x) / obj.scroll.scale / obj.ratio;
		var y = Math.abs(obj.scroll.y) / obj.scroll.scale / obj.ratio;

		ctx.drawImage(obj.img, x, y, w, h, 0, 0, _w, _h);
		return canvas.toDataURL();
	}

	function ajaxFileUpload(image, image_) {
		$.ajaxFileUpload({
			url : '/xdb/ajax/ajax!upLoad.do',// servlet请求路径
			secureuri : false,
			fileElementId : image,// 上传控件的id
			dataType : 'json',
			data : {
				paramName : image
			}, // 参数名称
			success : function(data, status) {
				$(image_).val(data.msg);
			},
			error : function(data, status, e) {
				alert('上传出错');
			}
		});

		return false;

	}
});
