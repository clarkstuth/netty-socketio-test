var files = null;

$(document).ready(function () {
    files = {
        waiting: $("#not-started"),
        running: $("#running"),
        complete: $("#complete")
    };

    setTimeout(function () {
        alert("test");
        addFile("4", "some/new/file/type.txt", "10-23-2015 2:45:PM", files.waiting);
        clearFileStatus(createFileHtmlId(2));
    }, 2000); // 2000 is 2 seconds...

});

function addFile(id, file, time, listSelector) {
    var htmlId = createFileHtmlId(id);
    var html = "<li id=\"" + htmlId + "\">" + file + " - " + time;
    listSelector.append(html);
}

function createFileHtmlId(id) {
    return "file_" + id
}

function clearFileStatus(fileId) {
    $("#" + fileId).remove();
}