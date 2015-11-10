import netty.socketio.test.DashboardUpdateService

class BootStrap {

    def DashboardUpdateService dashboardUpdateService

    def init = { servletContext ->
        dashboardUpdateService.start()
    }
    def destroy = {
        dashboardUpdateService.destroy()
    }
}