padApp.controller('ChangePasswordController', [ '$scope', '$rootScope', 'changePasswordService', function($scope, $rootScope, changePasswordService) {

    $scope.passwordRegexp = /(?=(:?.*[^A-Za-z0-9].*))(?=(:?.*[A-Z].*){1,})(?=(:?.*\d.*){1,})(:?^[\w\&\?\!\$\#\*\+\=\%\^\@\-\.\,\_]{8,32}$)/;
    $scope.formData = {};
    $scope.passwordChangeErrorResponse = "";
    $scope.passwdChangeSuccess = false;
    $scope.passwordChangeSubmitButtonDisabled = false;

    $scope.back = function() {
        $rootScope.go('/');
    }

    $scope.save = function() {
        $scope.changePasswordForm.$setPristine();

        if ($scope.changePasswordForm.currentPassword.$invalid) {
            $scope.passwordChangeErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CURRENT_PASSWORD_MESSAGE + ".";
            return;

        } else if (!$scope.passwordRegexp.test($scope.formData.password)) {
            $scope.passwordChangeErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_PASSWORD_MESSAGE + ".";
            return;

        } else if ($scope.changePasswordForm.confirmPassword.$invalid) {
            $scope.passwordChangeErrorResponse = $rootScope.translation.KEY_SCREEN_ENTER_VALID_CONFIRM_PASSWORD_MESSAGE + ".";
            return;

        } else if ($scope.formData.password !== $scope.formData.confirmPassword) {
            $scope.passwordChangeErrorResponse = $rootScope.translation.KEY_SCREEN_CONFIRM_PASSWORD_NOT_MATCH_MESSAGE + ".";
            return;

        } else {
            
            $scope.passwordChangeSubmitButtonDisabled = true;

            $rootScope.startSpinner();

            var passwordData = {
                currentPassword : $scope.formData.currentPassword,
                password : $scope.formData.password,
                confirmPassword : $scope.formData.confirmPassword
            };

            changePasswordService.changePassword(passwordData, function(response) {

                if (response.responseCode === 0) {
                    $scope.passwdChangeSuccess = true;
                    $scope.passwordChangeErrorResponse = "";
                } else {
                    $scope.passwordChangeErrorResponse = $rootScope.getTranslationStringByResponseCode(response.responseCode) + ".";
                    $scope.passwordChangeSubmitButtonDisabled = false;
                }
                $rootScope.stopSpinner();

            }, function(error) {

                console.log(error);

                $rootScope.stopSpinner();
                $scope.passwordChangeErrorResponse = $rootScope.getTranslationStringByResponseCode(error.data.responseCode) + ".";
                $scope.passwordChangeSubmitButtonDisabled = false;
            });
        }
    }

    $(function() {
        $('[data-toggle="tooltip"]').tooltip()
    });

} ]);
