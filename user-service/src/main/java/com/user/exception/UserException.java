package com.user.exception;

public class UserException extends Exception {
    public static class HandleException extends RuntimeException {
        public HandleException(String message) {
            super(message);
        }
    }

    public static class GetUserByIdHandler extends RuntimeException {
        public GetUserByIdHandler(String message) {
            super(message);
        }
    }

    public static class VerifyAccountHandler extends RuntimeException{
        public VerifyAccountHandler(String message){
            super(message);
        }
    }

    public static class UpdateHandler extends RuntimeException{
        public UpdateHandler(String message){
            super(message);
        }
    }

    public static class DeleteHandler extends RuntimeException{
        public DeleteHandler(String message){
            super(message);
        }
    }

    public static class ForgetPasswordHandler extends RuntimeException{
        public ForgetPasswordHandler(String message){
            super(message);
        }
    }

    public static class ResetPasswordHandler extends RuntimeException{
        public ResetPasswordHandler(String message){
            super(message);
        }
    }

    public static class UpdateEamilHandler extends RuntimeException{
        public UpdateEamilHandler(String message){
            super(message);
        }
    }
}
