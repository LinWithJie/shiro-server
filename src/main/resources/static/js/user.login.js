
//换种方式获取，之前的方式在不同的环境下，有问题
var baseUrl = $("script[baseUrl]").attr('baseUrl');
/**退出*/
function logout(){
    var load = layer.load();
    window.location.href=baseUrl+"/logout";
}
