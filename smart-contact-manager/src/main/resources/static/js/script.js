console.log("this is script file")

const toggleSideBar = () => {
    if($(".sidebar").is(":visible")) { //we close it here
        
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");

    }

    else { // we open it here

        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");

    }
}

console.log("this is script file")


