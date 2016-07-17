function AntispoofGps(){

}

AntispoofGps.prototype.checkGpsSpoof = function(successCallback, errorCallback){
  cordova.exec(successCallback, errorCallback, "AntispoofGps", "checkGpsSpoof",[])
};

AntispoofGps.install = function(){
  if(!window.plugins){
    window.plugins = {};
  }

  window.plugins.antispoofgps = new AntispoofGps();

  return window.plugins.antispoofgps;
};

cordova.addConstructor(AntispoofGps.install);
