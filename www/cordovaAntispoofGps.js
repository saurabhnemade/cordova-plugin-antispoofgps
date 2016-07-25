function AntispoofGps(){

}

AntispoofGps.prototype.checkGpsSpoof = function(successCallback, errorCallback){
  cordova.exec(successCallback, errorCallback, "AntispoofGps", "checkGpsSpoof",[])
};

AntispoofGps.prototype.checkAllowMockLocationEnabled = function(successCallback, errorCallback){
  cordova.exec(successCallback, errorCallback, "AntispoofGps", "checkAllowMockLocationEnabled", []);
};

AntispoofGps.prototype.checkMockPermissionApps = function(successCallback, errorCallback){
  cordova.exec(successCallback, errorCallback, "AntispoofGps", "checkMockPermissionApps", []);
};

AntispoofGps.install = function(){
  if(!window.plugins){
    window.plugins = {};
  }

  window.plugins.antispoofgps = new AntispoofGps();

  return window.plugins.antispoofgps;
};

cordova.addConstructor(AntispoofGps.install);
