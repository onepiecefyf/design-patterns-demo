/**
 * Created by july_whj on 2018/10/10.
 */

var app = angular.module('ngRouteExample', ["ui.bootstrap", "ngRoute"])
.config(function ($routeProvider) {
  $routeProvider.when('/template', {
    templateUrl: 'view/template/template.html'
  }).when('/login', {
    templateUrl: 'login.html',
  }).otherwise({
    redirectTo: 'login'
  });
});

app.service("template", function templateIng() {
  var template = this;
  template.appId = "888888";
  template.tempId = "111";
  template.shapeEnum = "circle";
  template.height = 236;
  template.width = 472;
  template.stroke = 24;
  template.centerX = 0;
  template.centerY = 0;
  template.color = "rgb(255,0,0)";
  template.rx = 0;
  template.ry = 0;
  template.dpi = 600;
})

app.controller('imageController', function ($scope, $http, template) {
  $scope.template = template;
  $scope.showData = function () {
    console.log(template)
  }
  $scope.calculate = function () {
    $scope.height = (template.height / template.dpi) * 2.54;
    $scope.width = (template.width / template.dpi) * 2.54;
  }

  $scope.dynamicUpdate = function () {
    $scope.calculate();
    $scope.changeSeal();
  }

  $scope.changeSeal = function () {
    $http({
      url: 'http://localhost:8080/template/v1/image?appId=123',
      method: 'post',
      data: template
    }).then(function successCallback(response) {
      $scope.imageBase64 = response.data.data;
    }, function errorCallback(response) {
      // 请求失败执行代码
    });
  }

})

app.controller('templateController', function ($scope, $uibModal, template) {
  $scope.riders = [];
  $scope.template = template;
  template.riderBeans = $scope.riders;
  $scope.addRider = function ($index) {
    $scope.riders.splice($index + 1, 0,
        {
          "order": 0,
          "appendixContent": "默认",
          "fontSize": 135,
          "fontWeight": "bold",
          "fontFamily": "KaiTi",
          "radian": "M35,170 L435,170",
          "color": "rgb(255,0,0)"
        });
  };
  $scope.open = function () {
    var modalInstance = $uibModal.open({
      templateUrl: 'view/template/riderdebug.html',
      // controller: ModalInstanceCtrl,
    });
    modalInstance.opened.then(function () {//模态窗口打开之后执行的函数
      console.log('modal is opened');
    });
    modalInstance.result.then(function (result) {
      console.log(result);
    }, function (reason) {
      console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
    });
  };

  $scope.delRider = function ($index) {
    $scope.riders.splice($index, 1);
  };

})
