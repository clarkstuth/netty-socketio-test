package netty.socketio.test

class DashboardController {

    def index() {

        response.setHeader("Access-Control-Allow-Origin", "*")

        [files: [[id: 1, file: 'src/test/myfile.txt', time: '10-23-2015 1:51:22PM'],
                 [id: 2, file: 'src/test/someOtherFile.txt', time: '10-23-2015 1:53:00PM'],
                 [id: 3, file: 'src/test/theLastFile.txt', time: '10-23-2015 1:53:00PM']]]
    }
}