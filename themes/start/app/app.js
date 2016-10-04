(function($) {
  "use strict";
 

})(jQuery);


var codehelixUtils = {
		
	updateNav: function(items, page) {
	  var html = "";
	  for(var ndx = 0; ndx < items.length; ndx++){
		  var item = items[ndx];
		  if(item.name.toLowerCase() == page.toLowerCase()){
			  html += '<li class="active">'; 
			  
			  if( item.children != null && item.children.length > 0 ) {
			    html += this.buildMenuIndicator(item);
				html += this.buildMenu( item.children, 'dropdown-menu' );
			  }
			  else {
				html += '<a href="' + item.page + '" class="current">' + item.name + '</a>';
			  }
			  html += '</li>';
		  }
		  else {
			  html += '<li>'; 
			  if( item.children != null && item.children.length > 0 ) {
				html += this.buildMenuIndicator(item);
				html += this.buildMenu( item.children, 'dropdown-menu' );
			  }
			  else {
				html += '<a href="' + item.page + '">' + item.name + '</a>';
			  }
			  html += '</li>';
		  }
	  }
	  $("#navigation").html(html)
	},
	
	
	buildMenuIndicator: function(item) {
		return '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="true">'
				+ item.name 
				+ ' &nbsp;<span class="caret"></span></a>';
	},
	
	
	buildMenu: function(items, cls) {
	  var html = '<ul class="' + cls + '">';
	  for(var ndx = 0; ndx < items.length; ndx++){
		  var item = items[ndx];
		  html += '<li><a href="' + item.page + '">' + item.name + '</a></li>';
	  }
	  html += "</ul>";
	  return html;
	},
	
	
	updateTemplate: function() {
	  
	  // Header 
	  $("#topbar_title" ).html( codeHelixMeta.product.questions );  
	  $("#topbar_social_facebook" ).attr('href', codeHelixMeta.social.facebook); 
	  $("#topbar_social_twitter"  ).attr('href', codeHelixMeta.social.twitter);   
	  $("#topbar_social_gplus"    ).attr('href', codeHelixMeta.social.gplus);   
	  $("#topbar_social_pinterest").attr('href', codeHelixMeta.social.linkedin); 
	  
	  // Footer 
	  $("#footer_aboutus").html('Slate Kit is a product of <a href="www.codehelix.co">Code Helix Solutions Inc.</a>. We specialise in contract services focused on Scala, Java, PHP for Web and Mobile Applications');
	  $("#footer_needhelp").html('For more information on Slate Kit, including source code visit our github repository and feel free to email/contact us at <a href="mailto:kishore@codehelix.co">kishore@codehelix.co</a>');
	  $("#footer_addr_city").html(codeHelixMeta.company.address.city);
	  $("#footer_addr_region").html(codeHelixMeta.company.address.region);
	  $("#footer_addr_street").html(codeHelixMeta.company.address.street);
	  $("#footer_addr_country").html(codeHelixMeta.company.address.country);
	  $("#footer_addr_zip").html(codeHelixMeta.company.address.zip);
	  $("#footer_social_facebook" ).attr('href', codeHelixMeta.social.facebook); 
	  $("#footer_social_twitter"  ).attr('href', codeHelixMeta.social.twitter);   
	  $("#footer_social_gplus"    ).attr('href', codeHelixMeta.social.gplus);   
	  $("#footer_social_pinterest").attr('href', codeHelixMeta.social.linkedin); 
	}
};


var codeHelixMeta = {
	
  company: 
  {
	  name: "Code Helix",
	  site: "www.codehelix.co",
	  contact: "kishore@codehelix.co",
	  address: 
	  {
		  street: "",
		  city: "Queens",
		  region: "New York",
		  state: "New York",
		  zip: "",
		  country: "U.S.A."
	  }
  },
  
  social: 
  {
	facebook: "http://www.facebook.com",
	linkedin: "http://www.linkedin.com",
	twitter: "http://www.twitter.com",
	gplus: "http://www.google.com/plus"
  },
  
  
  product: 
  {
	name : "Slate Kit",
	slogan: "A scala toolkit, utility library and server backend",
	about: 'Slate Kit is a product of <a href="www.codehelix.co">Code Helix Solutions Inc.</a>. We specialise in contract services focused on Scala, Java, PHP for Web and Mobile Applications',
	needhelp: 'For more information on Slate Kit, including source code visit our github repository and feel free to email/contact us at <a href="mailto:kishore@codehelix.co">kishore@codehelix.co</a>',
	questions: 'Questions ? Contact us via github or at <a href="http://www.codehelix.co">Code Helix Solutions Inc.</a>'
  },
  
  menu: 
  [
	{ name: "Home"          ,  page: "index.html"       , children: null },
	{ name: "Overview"      ,  page: "overview.html"    , children: null },
	{ name: "Setup"         ,  page: "setup.html"       , children: null },
	{ name: "Core"          ,  page: "infra.html"       , children: null },
	{ name: "App"           ,  page: "app.html"         , children: null },
	{ name: "APIs"          ,  page: "apis-detail.html" , children: null },
	{ name: "Server"        ,  page: "server.html"      , children: null },
	{ name: "ORM"           ,  page: "orm.html"         , children: null },
	{ name: "Utils"         ,  page: "utils.html"       , children: null },
	{ name: "Extensions"      ,  page: "features.html"    , children: null },
	{ name: "Releases"      ,  page: "releases.html"    , children: null },
	{ name: "Docs"          ,  page: "docs/slate-scala-docs.zip" },
	{ name: "More"          ,  page: "-"                , children: [
		{ name: "Scala 101"        ,  page: "scala101.html"    },
		{ name: "Sample Apps"      ,  page: "samples.html"     },
		{ name: "About Us"         ,  page: "about.html"       }
	  ]
	}
  ]
};  

console.log("slogan: " + codeHelixMeta.slogan);
console.log("page  : " + _codehelixAppPage);
codehelixUtils.updateNav(codeHelixMeta.menu, _codehelixAppPage);
codehelixUtils.updateTemplate();



