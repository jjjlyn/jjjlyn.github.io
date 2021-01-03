// A $( document ).ready() block.
$( document ).ready(function() {

	// DropCap.js
	var dropcaps = document.querySelectorAll(".dropcap");
	window.Dropcap.layout(dropcaps, 2);

	// Responsive-Nav
	var nav = responsiveNav(".nav-collapse");

	// Round Reading Time
    $(".time").text(function (index, value) {
      return Math.round(parseFloat(value));
    });

});

$("[data-tag]").click((e) => {
	currentTag = e.target.dataset.tag;
	filterByTagName(currentTag);
  })
  
  function filterByTagName(tagName) {
	$('.hidden').removeClass('hidden');
	$('.postWrapper').each((index, elem) => {
	  if (!elem.hasAttribute(`data-${tagName}`)) {
		$(elem).addClass('hidden');
	  }
	});
	$(`.tag`).removeClass('selected');
	$(`.tag[data-tag=${tagName}]`).addClass('selected');
  }


