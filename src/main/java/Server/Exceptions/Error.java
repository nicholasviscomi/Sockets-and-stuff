package Server.Exceptions;

import Server.Server;

public enum Error {
    NoSuchUser,
    InvalidUsername,
    UserAlreadyConnected,
    Nil;
}

//class ServerErrors {
//
//    public static int toInt(Server.Error e) {
//        switch (e) {
//            case NoSuchUser:
//                return 0;
//            case InvalidUsername:
//                return 1;
//            default:
//                return -1;
//        }
//    }
//
//    public static Server.Error toError(int n) {
//        switch (n) {
//            case 0:
//                return Server.Error.NoSuchUser;
//            case 1:
//                return Server.Error.InvalidUsername;
//            default:
//                return Server.Error.Nil;
//        }
//    }
//}
