import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.MalformedJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TestSerializable
{
    String obj = "{\"e168\":\"c\",\"e169\":null,\"e166\":\"b\",\"e167\":null,\"e164\":\"a\",\"e165\":\"b\",\"e162\":1,\"e163\":[],\"e160\":null,\"e161\":100,\"e157\":\"c\",\"e399\":1,\"e158\":100,\"e155\":\"c\",\"e397\":\"d\",\"e156\":1,\"e398\":\"c\",\"e153\":100,\"e395\":\"a\",\"e154\":1,"
    + "\"e396\":1,\"e151\":{},\"e393\":[],\"e152\":\"d\",\"e394\":true,\"e391\":{},\"e150\":\"a\",\"e392\":false,\"e390\":1,\"e159\":\"d\",\"e188\":{},\"e189\":1,\"e186\":\"d\",\"e187\":false,\"e184\":100,\"e185\":null,\"e182\":100,\"e183\":true,\"e180\":\"c\",\"e181\":null,\"e179\":1,"
    + "\"e177\":{},\"e178\":\"c\",\"e175\":100,\"e176\":1,\"e173\":100,\"e174\":1,\"e171\":\"c\",\"e172\":100,\"e170\":1,\"e199\":true,\"e197\":\"a\",\"e198\":1,\"e195\":100,\"e196\":false,\"e193\":100,\"e194\":\"d\",\"e191\":null,\"e192\":\"b\",\"e190\":\"c\",\"e11\":\"c\",\"e10\":1,\"e13\":1,"
    + "\"e12\":\"d\",\"e15\":\"c\",\"e14\":true,\"e17\":100,\"e16\":null,\"e19\":\"b\",\"e18\":\"d\",\"e20\":true,\"e22\":1,\"e21\":\"b\",\"e24\":{},\"e23\":null,\"e26\":\"a\",\"e25\":\"c\",\"e28\":\"b\",\"e27\":\"a\",\"e29\":1,\"e31\":\"a\",\"e30\":\"d\",\"e33\":100,\"e32\":{},\"e35\":\"b\","
    + "\"e34\":\"b\",\"e37\":false,\"e36\":null,\"e39\":false,\"e38\":true,\"e809\":null,\"e807\":\"a\",\"e808\":\"a\",\"e805\":null,\"e806\":null,\"e803\":true,\"e804\":\"b\",\"e801\":\"b\",\"e802\":{},\"e800\":true,\"e40\":100,\"e42\":\"c\",\"e41\":\"a\",\"e44\":1,\"e43\":100,\"e46\":\"a\","
    + "\"e45\":{},\"e48\":\"b\",\"e47\":false,\"e49\":1,\"e51\":\"c\",\"e50\":\"d\",\"e53\":{},\"e52\":1,\"e55\":100,\"e54\":true,\"e57\":\"d\",\"e56\":\"b\",\"e59\":{},\"e58\":\"d\",\"e829\":\"c\",\"e827\":false,\"e828\":\"c\",\"e825\":true,\"e826\":[],\"e823\":1,\"e824\":1,\"e821\":\"b\","
    + "\"e822\":100,\"e60\":100,\"e820\":false,\"e62\":\"d\",\"e61\":\"c\",\"e64\":\"a\",\"e63\":null,\"e66\":null,\"e65\":\"d\",\"e68\":[],\"e67\":{},\"e69\":true,\"e818\":[],\"e819\":true,\"e816\":false,\"e817\":1,\"e814\":false,\"e815\":true,\"e812\":null,\"e813\":1,\"e810\":1,\"e811\":1,"
    + "\"e71\":100,\"e70\":1,\"e73\":\"a\",\"e72\":1,\"e840\":[],\"e75\":false,\"e74\":true,\"e77\":true,\"e76\":\"b\",\"e79\":1,\"e78\":null,\"e609\":true,\"e607\":false,\"e849\":1,\"e608\":1,\"e605\":false,\"e847\":\"a\",\"e606\":true,\"e848\":1,\"e603\":false,\"e845\":null,\"e604\":false,"
    + "\"e846\":[],\"e80\":null,\"e601\":null,\"e843\":100,\"e602\":\"b\",\"e844\":{},\"e82\":100,\"e841\":\"b\",\"e81\":1,\"e600\":\"c\",\"e842\":{},\"e84\":true,\"e83\":\"d\",\"e86\":1,\"e85\":\"a\",\"e88\":{},\"e87\":\"c\",\"e89\":null,\"e838\":\"d\",\"e839\":\"d\",\"e836\":false,"
    + "\"e837\":\"a\",\"e834\":1,\"e835\":true,\"e91\":1,\"e832\":true,\"e90\":1,\"e833\":1,\"e93\":{},\"e830\":1,\"e92\":null,\"e831\":\"c\",\"e95\":\"b\",\"e861\":{},\"e94\":[],\"e620\":\"c\",\"e862\":{},\"e97\":{},\"e96\":null,\"e860\":\"b\",\"e99\":\"a\",\"e98\":\"b\",\"e629\":100,"
    + "\"e627\":\"b\",\"e869\":\"a\",\"e628\":[],\"e625\":false,\"e867\":{},\"e626\":[],\"e868\":\"c\",\"e623\":1,\"e865\":false,\"e624\":100,\"e866\":\"b\",\"e621\":100,\"e863\":{},\"e622\":{},\"e864\":false,\"e850\":true,\"e851\":{},\"e618\":null,\"e619\":\"c\",\"e616\":{},\"e858\":\"b\","
    + "\"e617\":false,\"e859\":true,\"e614\":[],\"e856\":1,\"e615\":false,\"e857\":\"d\",\"e612\":1,\"e854\":\"c\",\"e613\":1,\"e855\":true,\"e610\":{},\"e852\":\"d\",\"e611\":true,\"e853\":100,\"e641\":\"a\",\"e883\":\"d\",\"e400\":\"d\",\"e642\":true,\"e884\":true,\"e881\":false,\"e640\":{},"
    + "\"e882\":\"a\",\"e880\":\"d\",\"e409\":1,\"e407\":\"d\",\"e649\":\"d\",\"e408\":[],\"e405\":\"b\",\"e647\":1,\"e889\":true,\"e406\":100,\"e648\":1,\"e403\":100,\"e645\":false,\"e887\":1,\"e404\":false,\"e646\":false,\"e888\":100,\"e401\":[],\"e643\":1,\"e885\":null,\"e402\":\"b\","
    + "\"e644\":false,\"e886\":1,\"e630\":1,\"e872\":\"b\",\"e631\":[],\"e873\":1,\"e870\":\"c\",\"e871\":\"a\",\"e638\":1,\"e639\":\"d\",\"e636\":100,\"e878\":null,\"e637\":\"b\",\"e879\":1,\"e634\":100,\"e876\":false,\"e635\":\"d\",\"e877\":\"a\",\"e632\":{},\"e874\":\"a\",\"e633\":100,"
    + "\"e875\":\"d\",\"e421\":\"c\",\"e663\":\"a\",\"e422\":{},\"e664\":true,\"e661\":[],\"e420\":100,\"e662\":null,\"e660\":1,\"e429\":1,\"e427\":null,\"e669\":\"a\",\"e428\":\"c\",\"e425\":[],\"e667\":\"c\",\"e426\":\"b\",\"e668\":\"c\",\"e423\":1,\"e665\":\"d\",\"e424\":1,\"e666\":true,"
    + "\"e410\":null,\"e652\":\"a\",\"e894\":1,\"e411\":\"d\",\"e653\":1,\"e895\":1,\"e650\":[],\"e892\":\"a\",\"e651\":\"d\",\"e893\":true,\"e890\":{},\"e891\":[],\"e418\":\"c\",\"e419\":\"b\",\"e416\":\"d\",\"e658\":1,\"e417\":\"c\",\"e659\":\"c\",\"e414\":\"b\",\"e656\":true,\"e898\":null,"
    + "\"e415\":\"c\",\"e657\":{},\"e899\":false,\"e412\":{},\"e654\":[],\"e896\":100,\"e413\":100,\"e655\":true,\"e897\":{},\"e201\":\"a\",\"e443\":\"b\",\"e685\":null,\"e202\":100,\"e444\":100,\"e686\":{},\"e441\":\"d\",\"e683\":1,\"e200\":[],\"e442\":true,\"e684\":\"a\",\"e681\":\"a\","
    + "\"e440\":\"c\",\"e682\":100,\"e680\":1,\"e209\":{},\"e207\":1,\"e449\":{},\"e208\":\"b\",\"e205\":{},\"e447\":\"b\",\"e689\":\"b\",\"e206\":\"b\",\"e448\":\"a\",\"e203\":\"b\",\"e445\":\"b\",\"e687\":{},\"e204\":[],\"e446\":null,\"e688\":1,\"e432\":\"b\",\"e674\":100,\"e433\":1,"
    + "\"e675\":false,\"e430\":null,\"e672\":\"c\",\"e431\":\"d\",\"e673\":[],\"e670\":100,\"e671\":\"a\",\"e438\":1,\"e439\":\"d\",\"e436\":\"d\",\"e678\":{},\"e437\":1,\"e679\":1,\"e434\":\"d\",\"e676\":1,\"e435\":\"c\",\"e677\":null,\"e223\":\"c\",\"e465\":\"d\",\"e224\":true,\"e466\":1,"
    + "\"e221\":1,\"e463\":null,\"e222\":1,\"e464\":\"b\",\"e461\":\"c\",\"e220\":null,\"e462\":\"a\",\"e460\":\"c\",\"e229\":false,\"e227\":1,\"e469\":true,\"e228\":\"c\",\"e225\":true,\"e467\":100,\"e226\":\"a\",\"e468\":1,\"e212\":\"a\",\"e454\":[],\"e696\":1,\"e213\":true,\"e455\":false,"
    + "\"e697\":1,\"e210\":\"a\",\"e452\":\"a\",\"e694\":\"b\",\"e211\":\"c\",\"e453\":\"d\",\"e695\":1,\"e450\":1,\"e692\":\"b\",\"e451\":\"c\",\"e693\":\"b\",\"e690\":null,\"e691\":1,\"e218\":1,\"e219\":false,\"e216\":\"b\",\"e458\":\"b\",\"e217\":true,\"e459\":\"a\",\"e214\":false,"
    + "\"e456\":\"b\",\"e698\":null,\"e215\":false,\"e457\":[],\"e699\":100,\"e245\":false,\"e487\":false,\"e246\":\"a\",\"e488\":{},\"e243\":\"d\",\"e485\":\"b\",\"e244\":null,\"e486\":\"c\",\"e241\":[],\"e483\":\"d\",\"e242\":\"b\",\"e484\":true,\"e481\":1,\"e240\":\"c\",\"e482\":{},"
    + "\"e480\":\"b\",\"e249\":{},\"e247\":100,\"e489\":\"d\",\"e248\":\"d\",\"e234\":\"a\",\"e476\":{},\"e235\":true,\"e477\":true,\"e232\":\"a\",\"e474\":\"c\",\"e233\":\"c\",\"e475\":[],\"e230\":{},\"e472\":\"d\",\"e231\":\"b\",\"e473\":\"c\",\"e470\":\"a\",\"e471\":\"a\",\"e238\":\"c\","
    + "\"e239\":{},\"e236\":{},\"e478\":\"d\",\"e237\":{},\"e479\":\"a\",\"e267\":\"c\",\"e268\":\"a\",\"e265\":true,\"e266\":false,\"e263\":\"c\",\"e264\":1,\"e261\":\"d\",\"e262\":\"d\",\"e260\":true,\"e269\":\"a\",\"e256\":1,\"e498\":{},\"e257\":\"b\",\"e499\":{},\"e254\":\"c\",\"e496\":[],"
    + "\"e255\":100,\"e497\":1,\"e252\":null,\"e494\":null,\"e253\":\"d\",\"e495\":{},\"e250\":true,\"e492\":1,\"e251\":{},\"e493\":null,\"e490\":{},\"e491\":\"c\",\"e258\":100,\"e259\":\"c\",\"e289\":1,\"e287\":1,\"e288\":null,\"e285\":\"b\",\"e286\":false,\"e283\":{},\"e284\":\"b\","
    + "\"e281\":true,\"e282\":1,\"e280\":[],\"e278\":1,\"e279\":false,\"e276\":false,\"e277\":\"b\",\"e274\":null,\"e275\":100,\"e272\":true,\"e273\":null,\"e270\":1,\"e271\":1,\"e298\":\"a\",\"e299\":false,\"e296\":\"d\",\"e297\":null,\"e294\":100,\"e295\":1,\"e292\":\"d\",\"e293\":\"c\","
    + "\"e290\":null,\"e0\":\"b\",\"e291\":false,\"e1\":\"a\",\"e2\":\"b\",\"e3\":1,\"e4\":100,\"e5\":true,\"e6\":\"c\",\"e7\":false,\"e8\":\"c\",\"e9\":true,\"e908\":\"c\",\"e909\":false,\"e906\":1,\"e907\":[],\"e904\":\"d\",\"e905\":\"d\",\"e902\":{},\"e903\":\"b\",\"e900\":true,\"e901\":1,"
    + "\"e928\":{},\"e929\":[],\"e926\":null,\"e927\":[],\"e924\":\"d\",\"e925\":false,\"e922\":false,\"e923\":\"c\",\"e920\":\"b\",\"e921\":\"c\",\"e919\":true,\"e917\":1,\"e918\":true,\"e915\":\"a\",\"e916\":{},\"e913\":\"a\",\"e914\":1,\"e911\":\"c\",\"e912\":true,\"e910\":1,\"e708\":\"c\","
    + "\"e709\":\"a\",\"e706\":100,\"e948\":null,\"e707\":false,\"e949\":\"d\",\"e704\":\"b\",\"e946\":null,\"e705\":null,\"e947\":100,\"e702\":1,\"e944\":{},\"e703\":null,\"e945\":[],\"e700\":\"d\",\"e942\":{},\"e701\":\"a\",\"e943\":[],\"e940\":[],\"e941\":1,\"e939\":\"b\",\"e937\":null,"
    + "\"e938\":true,\"e935\":[],\"e936\":{},\"e933\":true,\"e934\":\"c\",\"e931\":\"c\",\"e932\":100,\"e930\":\"b\",\"e960\":\"a\",\"e961\":1,\"e728\":null,\"e729\":100,\"e726\":\"a\",\"e968\":[],\"e727\":[],\"e969\":true,\"e724\":\"a\",\"e966\":1,\"e725\":1,\"e967\":\"b\",\"e722\":[],"
    + "\"e964\":100,\"e723\":true,\"e965\":\"d\",\"e720\":\"d\",\"e962\":1,\"e721\":\"a\",\"e963\":\"c\",\"e950\":\"a\",\"e719\":\"b\",\"e717\":null,\"e959\":false,\"e718\":null,\"e715\":\"c\",\"e957\":100,\"e716\":100,\"e958\":1,\"e713\":1,\"e955\":null,\"e714\":100,\"e956\":null,\"e711\":\"a\","
    + "\"e953\":null,\"e712\":\"a\",\"e954\":\"d\",\"e951\":true,\"e710\":\"a\",\"e952\":{},\"e740\":true,\"e982\":{},\"e741\":\"d\",\"e983\":null,\"e980\":\"d\",\"e981\":false,\"e508\":1,\"e509\":1,\"e506\":\"b\",\"e748\":null,\"e507\":100,\"e749\":\"d\",\"e504\":\"d\",\"e746\":true,"
    + "\"e988\":\"a\",\"e505\":null,\"e747\":\"d\",\"e989\":1,\"e502\":1,\"e744\":\"a\",\"e986\":100,\"e503\":1,\"e745\":100,\"e987\":\"a\",\"e500\":1,\"e742\":{},\"e984\":[],\"e501\":\"c\",\"e743\":\"d\",\"e985\":{},\"e971\":1,\"e730\":\"b\",\"e972\":\"a\",\"e970\":1,\"e739\":\"a\",\"e737\":{},"
    + "\"e979\":true,\"e738\":[],\"e735\":1,\"e977\":\"a\",\"e736\":[],\"e978\":false,\"e733\":\"b\",\"e975\":\"b\",\"e734\":\"b\",\"e976\":\"a\",\"e731\":1,\"e973\":[],\"e732\":1,\"e974\":\"c\",\"e520\":100,\"e762\":\"a\",\"e521\":null,\"e763\":100,\"e760\":\"b\",\"e761\":{},\"e528\":1,"
    + "\"e529\":[],\"e526\":\"d\",\"e768\":\"a\",\"e527\":null,\"e769\":\"c\",\"e524\":\"c\",\"e766\":\"a\",\"e525\":\"b\",\"e767\":[],\"e522\":\"a\",\"e764\":{},\"e523\":\"a\",\"e765\":1,\"e751\":1,\"e993\":1,\"e510\":\"c\",\"e752\":true,\"e994\":{},\"e991\":{},\"e750\":[],\"e992\":100,"
    + "\"e990\":false,\"e519\":null,\"e517\":[],\"e759\":\"d\",\"e518\":1,\"e515\":100,\"e757\":\"b\",\"e999\":\"d\",\"e516\":[],\"e758\":true,\"e513\":1,\"e755\":false,\"e997\":\"a\",\"e514\":false,\"e756\":null,\"e998\":\"c\",\"e511\":\"a\",\"e753\":\"a\",\"e995\":true,\"e512\":{},\"e754\":100,"
    + "\"e996\":\"b\",\"e300\":\"c\",\"e542\":\"b\",\"e784\":[],\"e301\":1,\"e543\":\"a\",\"e785\":[],\"e540\":\"d\",\"e782\":\"b\",\"e541\":\"c\",\"e783\":{},\"e780\":\"d\",\"e781\":1,\"e308\":[],\"e309\":true,\"e306\":\"d\",\"e548\":false,\"e307\":null,\"e549\":1,\"e304\":\"c\",\"e546\":null,"
    + "\"e788\":null,\"e305\":\"b\",\"e547\":\"a\",\"e789\":1,\"e302\":100,\"e544\":true,\"e786\":{},\"e303\":null,\"e545\":100,\"e787\":100,\"e531\":1,\"e773\":\"a\",\"e532\":1,\"e774\":[],\"e771\":1,\"e530\":true,\"e772\":100,\"e770\":{},\"e539\":\"b\",\"e537\":1,\"e779\":[],\"e538\":1,"
    + "\"e535\":100,\"e777\":100,\"e536\":100,\"e778\":1,\"e533\":\"d\",\"e775\":{},\"e534\":100,\"e776\":1,\"e322\":true,\"e564\":100,\"e323\":\"d\",\"e565\":true,\"e320\":100,\"e562\":true,\"e321\":{},\"e563\":\"a\",\"e560\":true,\"e561\":\"a\",\"e328\":1,\"e329\":1,\"e326\":null,\"e568\":true,"
    + "\"e327\":null,\"e569\":1,\"e324\":null,\"e566\":false,\"e325\":null,\"e567\":false,\"e311\":\"b\",\"e553\":false,\"e795\":false,\"e312\":[],\"e554\":{},\"e796\":100,\"e551\":[],\"e793\":1,\"e310\":false,\"e552\":null,\"e794\":true,\"e791\":\"b\",\"e550\":1,\"e792\":\"c\",\"e790\":{},"
    + "\"e319\":true,\"e317\":1,\"e559\":[],\"e318\":null,\"e315\":\"b\",\"e557\":\"b\",\"e799\":\"d\",\"e316\":\"a\",\"e558\":100,\"e313\":\"d\",\"e555\":\"c\",\"e797\":\"b\",\"e314\":\"d\",\"e556\":{},\"e798\":[],\"e102\":\"d\",\"e344\":1,\"e586\":false,\"e103\":true,\"e345\":\"d\","
    + "\"e587\":true,\"e100\":100,\"e342\":[],\"e584\":[],\"e101\":false,\"e343\":1,\"e585\":{},\"e340\":null,\"e582\":\"c\",\"e341\":\"d\",\"e583\":{},\"e580\":\"d\",\"e581\":100,\"e108\":false,\"e109\":1,\"e106\":1,\"e348\":{},\"e107\":true,\"e349\":\"b\",\"e104\":false,\"e346\":1,"
    + "\"e588\":null,\"e105\":true,\"e347\":1,\"e589\":true,\"e333\":1,\"e575\":\"a\",\"e334\":\"a\",\"e576\":100,\"e331\":{},\"e573\":1,\"e332\":100,\"e574\":100,\"e571\":100,\"e330\":false,\"e572\":[],\"e570\":false,\"e339\":100,\"e337\":true,\"e579\":false,\"e338\":\"c\",\"e335\":100,"
    + "\"e577\":false,\"e336\":false,\"e578\":\"a\",\"e124\":1,\"e366\":100,\"e125\":\"a\",\"e367\":null,\"e122\":[],\"e364\":1,\"e123\":null,\"e365\":\"b\",\"e120\":{},\"e362\":false,\"e121\":1,\"e363\":100,\"e360\":\"d\",\"e361\":{},\"e128\":true,\"e129\":\"b\",\"e126\":1,\"e368\":{},"
    + "\"e127\":{},\"e369\":1,\"e113\":[],\"e355\":{},\"e597\":{},\"e114\":null,\"e356\":{},\"e598\":[],\"e111\":{},\"e353\":[],\"e595\":[],\"e112\":false,\"e354\":{},\"e596\":false,\"e351\":100,\"e593\":true,\"e110\":{},\"e352\":\"c\",\"e594\":true,\"e591\":true,\"e350\":\"d\",\"e592\":null,"
    + "\"e590\":false,\"e119\":[],\"e117\":\"a\",\"e359\":{},\"e118\":false,\"e115\":\"b\",\"e357\":true,\"e599\":{},\"e116\":\"c\",\"e358\":\"a\",\"e146\":[],\"e388\":\"a\",\"e147\":\"b\",\"e389\":100,\"e144\":{},\"e386\":{},\"e145\":\"d\",\"e387\":{},\"e142\":1,\"e384\":{},\"e143\":\"b\","
    + "\"e385\":\"a\",\"e140\":\"d\",\"e382\":100,\"e141\":1,\"e383\":true,\"e380\":\"b\",\"e381\":\"d\",\"e148\":1,\"e149\":[],\"e135\":\"a\",\"e377\":\"b\",\"e136\":\"b\",\"e378\":true,\"e133\":null,\"e375\":1,\"e134\":{},\"e376\":null,\"e131\":false,\"e373\":true,\"e132\":1,\"e374\":1,"
    + "\"e371\":\"a\",\"e130\":false,\"e372\":null,\"e370\":100,\"e139\":{},\"e137\":\"d\",\"e379\":1,\"e138\":\"c\"}";


    String arr = "[1,[],{},\"b\",true,{},{},1,\"d\",100,100,100,null,null,1,null,{},\"b\",{},{},\"b\",100,\"c\",\"a\",false,\"a\",{},1,\"a\",1,1,true,{},1,\"d\",\"d\",\"a\",null,\"d\",1,1,\"c\",1,\"b\",\"c\",true,null,{},{},true,1,\"a\",\"a\",\"d\",100,true,\"c\",{},true,null,1,100,1,100,1,true,"
    + "true,null,\"d\",false,\"b\",\"a\",false,false,\"a\",1,\"d\",\"c\",\"d\",100,\"d\",true,\"a\",[],[],\"b\",100,\"d\",100,[],\"c\",\"a\",1,[],{},1,\"d\",1,{},[],false,\"d\",[],null,\"d\",true,\"d\",100,1,1,{},null,\"b\",1,100,null,\"c\",true,null,1,100,100,{},[],\"c\",[],false,\"a\",{},\"c\","
    + "\"b\",\"c\",1,true,\"b\",[],\"d\",{},null,\"a\",\"d\",1,\"b\",\"b\",null,\"c\",100,\"b\",1,[],1,1,\"d\",{},null,true,[],\"c\",\"a\",[],1,false,{},[],\"b\",\"b\",[],{},true,100,null,\"d\",false,null,100,\"a\",\"d\",[],100,1,false,100,\"a\",\"a\",\"d\",\"b\",\"d\",{},\"a\",{},[],true,[],"
    + "true,\"d\",null,\"b\",[],\"a\",\"d\",[],true,1,\"d\",\"a\",\"b\",\"c\",1,\"c\",\"a\",[],\"d\",1,false,\"d\",false,1,1,\"c\",\"b\",100,\"c\",1,1,null,false,1,false,\"b\",{},100,null,1,100,false,1,{},1,\"a\",\"a\",\"a\",\"d\",\"d\",1,\"c\",\"b\",\"b\",\"a\",{},100,\"a\",\"c\",\"d\",1,100,"
    + "null,1,\"a\",100,1,false,\"c\",null,\"d\",1,\"b\",false,1,\"c\",false,\"c\",true,\"b\",[],{},1,{},100,1,100,\"c\",100,{},\"c\",null,1,\"a\",false,\"b\",1,false,\"c\",[],\"d\",1,true,\"c\",100,1,\"a\",\"c\",\"b\",1,100,1,false,\"b\",{},\"b\",1,true,\"d\",\"c\",1,\"b\",1,\"a\",\"a\",\"d\","
    + "{},null,[],\"c\",[],\"c\",true,null,1,false,\"a\",\"b\",1,100,\"b\",\"a\",false,1,true,\"d\",100,\"b\",false,null,null,100,\"c\",1,1,[],\"a\",100,[],\"d\",true,100,100,[],false,100,{},100,false,null,null,{},100,1,100,\"b\",{},1,\"a\",\"c\",\"d\",100,\"a\",false,1,\"d\",[],100,\"d\",100,1,"
    + "100,\"d\",null,\"b\",1,\"a\",1,null,\"b\",{},\"c\",\"a\",[],{},false,true,100,\"c\",1,[],[],1,\"d\",true,\"b\",false,{},[],\"d\",1,{},\"c\",\"c\",\"b\",false,{},100,{},[],1,null,{},1,1,\"c\",[],1,[],\"b\",\"d\",1,\"c\",[],1,\"d\",\"d\",[],100,\"b\",\"a\",null,100,true,100,100,\"b\",\"d\","
    + "100,false,false,\"c\",\"a\",1,\"a\",\"a\",{},true,100,true,\"b\",1,true,true,null,1,null,{},\"a\",{},\"a\",100,\"c\",true,{},100,1,\"d\",true,true,[],1,null,\"b\",[],\"b\",1,false,\"a\",\"c\",\"d\",\"a\",[],\"b\",\"c\",true,1,[],false,100,\"a\",\"c\",[],true,\"d\",\"a\",{},\"a\",1,null,"
    + "\"c\",true,1,true,{},\"c\",\"b\",[],1,100,100,\"b\",1,null,\"a\",\"b\",false,null,false,\"a\",1,\"c\",1,false,\"b\",false,{},100,\"a\",\"d\",\"d\",false,{},true,100,null,1,true,null,false,\"b\",{},100,[],[],1,\"c\",[],false,true,\"d\",\"b\",null,[],true,[],\"b\",true,\"c\",\"d\",true,{},"
    + "false,false,1,null,true,{},\"c\",[],[],{},false,false,100,\"d\",\"c\",\"d\",100,100,1,\"a\",{},true,true,1,{},{},null,1,1,\"d\",{},[],1,null,false,\"a\",1,[],true,100,\"c\",\"d\",\"b\",{},1,1,\"b\",1,{},{},\"c\",false,\"b\",[],\"d\",false,\"b\",null,false,true,null,true,{},\"b\",[],[],"
    + "\"c\",false,100,\"b\",100,null,1,1,\"a\",[],100,true,[],null,\"b\",{},[],\"d\",\"b\",\"b\",\"a\",\"d\",100,false,null,\"c\",{},true,100,\"d\",false,100,null,false,1,true,\"d\",\"d\",\"a\",null,{},null,\"b\",\"a\",[],100,1,false,1,\"c\",true,true,{},[],1,{},1,\"b\",\"c\",{},{},\"b\",\"b\","
    + "null,1,true,\"d\",[],{},null,[],\"a\",\"c\",[],100,\"c\",\"a\",\"a\",{},1,{},100,\"a\",false,null,1,100,null,false,\"d\",\"a\",\"a\",100,\"a\",\"b\",\"d\",null,100,100,100,1,100,\"a\",[],\"b\",1,\"d\",{},[],\"b\",null,true,{},\"a\",\"c\",1,{},\"c\",null,\"a\",1,\"d\",100,\"d\",[],\"a\",1,"
    + "\"a\",100,\"a\",\"b\",[],null,true,\"b\",1,true,false,false,\"b\",true,false,\"c\",{},\"a\",\"c\",\"d\",\"c\",false,100,false,null,\"d\",false,100,1,true,\"b\",[],\"d\",false,false,[],\"c\",\"a\",\"a\",\"d\",\"b\",100,true,1,\"a\",\"b\",\"d\",\"b\",\"b\",\"c\",\"c\",false,100,true,{},100,"
    + "true,\"b\",1,null,false,100,null,100,\"c\",\"d\",true,1,1,false,1,false,{},[],\"d\",null,100,\"b\",true,[],{},false,\"d\",\"c\",true,1,\"c\",null,\"d\",[],{},true,{},{},{},true,100,true,1,null,\"d\",null,\"a\",{},null,\"c\",false,1,\"c\",\"d\",{},100,\"d\",\"a\",\"b\",true,\"a\",100,null,"
    + "true,\"b\",null,\"a\",null,\"a\",\"b\",\"c\",false,{},{},\"c\",\"b\",100,100,false,\"c\",true,[],[],null,\"b\",\"a\",null,true,\"c\",[],true,true,false,1,\"b\",false,1,\"b\",\"c\",{},{},\"d\",false,{},false,\"b\",\"d\",[],\"b\",\"b\",{},\"c\",{},\"b\",\"a\",\"b\",false,\"c\",100,false,"
    + "\"a\",false,\"b\",1,1,null,{},true,true,1,1,\"c\",\"b\",null,false,\"d\",[],\"c\",\"d\",{},[],[],\"d\",100,\"a\",\"c\",\"d\",null,100,\"a\",null,100,1,\"d\",null,[],\"d\",\"a\",100,null,null,\"a\",null,\"b\",\"b\",true,1,\"d\",\"a\",100,{},\"c\",100,\"c\",false,null]";

    @Test
    public void test_mutable_json_obj() throws IOException, MalformedJson
    {

        JsObj json = JsObj._parse_(obj)
                          .orElseThrow();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream a = new ObjectOutputStream(out);

        a.writeObject(json);

        a.close();

        final JsObj deserialize = (JsObj) deserialize(out.toByteArray());

        Assertions.assertEquals(json,
                                deserialize);

    }

    @Test
    public void test_mutable_json_arr() throws IOException, MalformedJson
    {

        JsArray json = JsArray._parse_(arr)
                              .orElseThrow();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream a = new ObjectOutputStream(out);

        a.writeObject(json);

        a.close();

        final JsArray deserialize = (JsArray) deserialize(out.toByteArray());

        Assertions.assertEquals(json,
                                deserialize);

    }

    @Test
    public void test_immutable_json_obj() throws IOException, MalformedJson
    {

        JsObj json = JsObj.parse(obj)
                          .orElseThrow();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream a = new ObjectOutputStream(out);

        a.writeObject(json);

        a.close();

        final JsObj deserialize = (JsObj) deserialize(out.toByteArray());

        Assertions.assertEquals(json,
                                deserialize);

    }

    @Test
    public void test_immutable_json_arr() throws IOException, MalformedJson
    {

        JsArray json = JsArray.parse(arr)
                              .orElseThrow();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream a = new ObjectOutputStream(out);

        a.writeObject(json);

        a.close();

        final byte[] sf = out.toByteArray();
        final JsArray deserialize = (JsArray) deserialize(sf);

        Assertions.assertEquals(json,
                                deserialize);

    }


    private static Object deserialize(byte[] sf)
    {
        try
        {
            InputStream is = new ByteArrayInputStream(sf);
            ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}


