console.log("This is js file")


const toggleSidebar = () => {

    console.log("btn click")
    if($(".sidebar").is(":visible")) {

        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");

    }else{

        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");


    }
 }

