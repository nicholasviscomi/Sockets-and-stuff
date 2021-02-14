public enum Error {
    NoSuchUser,
    InvalidUsername,
    Nil;
}

class ServerErrors {
    public static int toInt(Error e) {
        switch (e) {
            case NoSuchUser:
                return 0;
            case InvalidUsername:
                return 1;
            default:
                return -1;
        }
    }

    public static Error toError(int n) {
        switch (n) {
            case 0:
                return Error.NoSuchUser;
            case 1:
                return Error.InvalidUsername;
            default:
                return Error.Nil;
        }
    }
}

class NoSuchUserException extends Exception {}