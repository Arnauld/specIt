$(document).ready(function () {
    var myTheme = $.cookie("doccoCodeTheme");
    if(typeof myTheme === "undefined" || null == myTheme)
    {
        myTheme = "Magula";
    }
    docco.switchStylesheet(myTheme);
});