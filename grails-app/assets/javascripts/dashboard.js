var files = null;

$(document).ready(function () {
    files = {
        queued: $("#queued"),
        processing: $("#processing"),
        complete: $("#complete")
    };

    var socket = io("http://localhost:9092");

    socket.on('fileProcessing', function (data) {
        handleFileUpdate(data.id, data.file, data.time, files.processing);
    });

    socket.on('fileCompleted', function (data) {
        handleFileUpdate(data.id, data.file, data.time, files.complete);
    });

    socket.on('fileQueued', function (data) {
        handleFileUpdate(data.id, data.file, data.time, files.queued);
    });

});

function handleFileUpdate(id, file, time, listSelector) {
    var htmlId = "file_" + id;
    $("#" + htmlId).remove();
    var html = "<li id=\"" + htmlId + "\">" + file + " - " + time;
    listSelector.append(html);
}