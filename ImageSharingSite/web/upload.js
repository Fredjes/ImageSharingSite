
var BASE_URL = "http://localhost:8080/ImageSharingSite/api/img";

onload = function () {
    document.getElementById("submit").onclick = sendFile;
};

function sendFile() {
    document.getElementById("status").innerHTML = "";

    var file = document.getElementById("filechooser").files[0];
    var extension = file.name.split(".").pop();

    var type;
    if (extension === "jpg" || extension === "jpeg" ||
            extension === "JPG" || extension === "JPEG") {
        type = "image/jpeg";
    } else if (extension === "png" || extension === "PNG") {
        type = "image/png";

    } else if (extension === "gif" || extension === "GIF") {
        type = "image/gif";
    } else {
        document.getElementById("status").innerHTML = "Invalid file type";
        type = "image/png";
        //return;
    }

    var request = new XMLHttpRequest();
    request.open("POST", BASE_URL);
    request.onload = function () {
        if (request.status === 201) {
            var fileName = request.getResponseHeader("Location").split("/").pop();
            document.getElementById("status").innerHTML = "File created with name <img src='" + BASE_URL + "/" + fileName + "' />";
        } else {
            document.getElementById("status").innerHTML = "Error creating file: (" + request.status + ") " + request.responseText;
        }
    };
    request.setRequestHeader("Content-Type", type);
    request.send(file);
}