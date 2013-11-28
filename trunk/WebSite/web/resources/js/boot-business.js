$("document").ready(function() {

	/*Start: Prevent the default white background on blur of top navigation */
	$(".dropdown-menu li a").mousedown(function() {
		var dropdown = $(this).parents('.dropdown');
		var link = dropdown.children(':first-child');
		link.css('background-color', "#2E3436");
		link.css('color', 'white');
	});
	/*End: Prevent the default white background on blur of top navigation */

  /*Start : Automatically start the slider */
	$('.carousel').carousel({
      interval: 3000
   });
	/*End: Automatically start the slider */

});
