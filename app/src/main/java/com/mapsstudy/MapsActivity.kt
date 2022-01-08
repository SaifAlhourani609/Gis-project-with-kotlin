package com.mapsstudy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.StrictMath.random

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapJordan: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var temp = 0
    lateinit var marker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        getSupportActionBar()?.hide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }

    private fun getLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 20000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        var markerOptions = MarkerOptions().position(latLng)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                        with(mMap) {
                            moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                            addMarker(markerOptions) //default marker set
                            setOnMapClickListener {
                                if(::marker.isInitialized){
                                    marker.remove()
                                    temp++
                                }
                                markerOptions.position(it)
                                marker = addMarker(markerOptions)
                                // ****** //
                                val num = intArrayOf(Color.RED,Color.GREEN,Color.BLUE,Color.CYAN,Color.GRAY,Color.MAGENTA)
                                // ****** //
                                val polygonOptions = PolygonOptions()
                                        .add(LatLng(location.latitude, location.longitude))
                                        .add(LatLng(marker.position.latitude, marker.position.longitude))
                                        .strokeWidth(3f)
                                        // ****** //
//                                        .strokeColor(num[temp])
                                        .strokeColor(num.random())

                                val polygon: Polygon = mMap.addPolygon(polygonOptions)
                            }
                        }

                        //بحسب المسافة بين نقطتين
                        // لم يتم تطبيقه
//                        val startPoint = Location("locationA")
//                        startPoint.setLatitude(location.latitude)
//                        startPoint.setLongitude(location.longitude)
//
//                        val endPoint = Location("locationB")
//                        endPoint.setLatitude(32.00296767194914)
//                        endPoint.setLongitude(35.94220581162739)
//                        val distance: Double = startPoint.distanceTo(endPoint)

//                        val polygonOptions = PolygonOptions()
//                                .add(LatLng(location.latitude, location.longitude))
//                                .add(LatLng(33.511097837613285, 36.2776191058705)) //دمشق
////                                .fillColor(Color.argb(0, 200, 0, 0))
//                                .strokeColor(Color.RED)
//                                .strokeWidth(3f) // ****** //
//
//                        val polygonOptions1 = PolygonOptions()
//                                .add(LatLng(location.latitude, location.longitude))
//                                .add(LatLng(33.31369894491618, 44.36171502967942)) //بغداد
////                                .fillColor(Color.argb(0, 50, 50, 0))
//                                .strokeColor(Color.GREEN)
//                                .strokeWidth(3f) // ****** //
//
//                        val polygonOptions2 = PolygonOptions()
//                                .add(LatLng(location.latitude, location.longitude))
//                                .add(LatLng(24.71201098077197, 46.67052182641138)) // الرياض
////                                .fillColor(Color.argb(0, 200, 0, 0))
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(3f) // ****** //

//                        val polygon: Polygon =
//                                mMap.addPolygon(polygonOptions)
//                                mMap.addPolygon(polygonOptions1)
//                                mMap.addPolygon(polygonOptions2)

                    }
                }
            }
        }
    }

    fun rest(view: View) {
        temp=-1
        for (i in 1..20)
            marker.remove()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getLocationAccess()
            }
            else {
                Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    fun Gulf(view: View) {
        bordersofthekingdomofjordan()
    }

    // حدود المملكة الاردنية
    fun bordersofthekingdomofjordan() {
        val polygonOptions = PolygonOptions()
                .add(LatLng(33.386188, 38.778873))
                .add(LatLng(32.50130327683556, 39.086639514515866))
                .add(LatLng(32.477531817617354, 38.98598092787838))
                .add(LatLng(32.304120693140526, 39.043670147481826))
                .add(LatLng(32.35559000568, 39.25998874416723))
                .add(LatLng(32.23027574785703, 39.3010738739992))
                .add(LatLng(31.99658637689263, 38.995946964434026))
                .add(LatLng(31.500125334173973, 37.000840594987956))
                .add(LatLng(30.500612855661767, 37.997154226503135)) //Close the polygon.
                .add(LatLng(30.332655369502582, 37.665535686037835)) //Close the polygon.
                .add(LatLng(30.000002940196975, 37.49999578998378)) //Close the polygon.
                .add(LatLng(29.86850302589196, 36.75352869750033)) //Close the polygon.
                .add(LatLng(29.50001689710515, 36.504540105997265)) //Close the polygon.
                .add(LatLng(29.18504381394657, 36.070086353376404)) //Close the polygon.
                .add(LatLng(29.357216437620806, 34.959548392636705)) //Close the polygon.
                .add(LatLng(29.52071119363462, 34.98392289947298)) //Close the polygon.
                .add(LatLng(29.53426081809708, 34.970080993248295)) //Close the polygon.
                .add(LatLng(29.541988753594104, 34.978060300943625)) //Close the polygon.
                .add(LatLng(29.576645492117745, 34.97801893932123)) //Close the polygon.
                .add(LatLng(29.59641752443156, 34.98717140018673)) //Close the polygon.
                .add(LatLng(29.621378368607374, 34.99969582040182)) //Close the polygon.
                .add(LatLng(29.633270287610483, 35.01000438146346)) //Close the polygon.
                .add(LatLng(29.633437769402608, 35.009522673002635)) //Close the polygon.
                .add(LatLng(29.640555488660112, 35.01482146622644)) //Close the polygon.
                .add(LatLng(29.65679875032786, 35.02002391760334)) //Close the polygon.
                .add(LatLng(29.674354109401108, 35.02097535896335)) //Close the polygon.
                .add(LatLng(29.6973044445053, 35.012331350952905)) //Close the polygon.
                .add(LatLng(29.714626300125847, 35.014258184819425)) //Close the polygon.
                .add(LatLng(29.77928487174861, 35.03256310635242)) //Close the polygon.
                .add(LatLng(29.79191059015579, 35.04528021001991)) //Close the polygon.
                .add(LatLng(29.80528709614567, 35.047014360478876)) //Close the polygon.
                .add(LatLng(29.8136465041143, 35.05202412847145)) //Close the polygon.
                .add(LatLng(29.821587295218368, 35.05125339503475)) //Close the polygon.
                .add(LatLng(29.86504149533764, 35.06965465832644)) //Close the polygon.
                .add(LatLng(29.8731452827023, 35.07871077764214)) //Close the polygon.
                .add(LatLng(29.879160038928017, 35.0788071193343)) //Close the polygon.
                .add(LatLng(29.88910029910308, 35.08458762086419)) //Close the polygon.
                .add(LatLng(29.924008651353954, 35.081408345115605)) //Close the polygon.
                .add(LatLng(29.926764052765353, 35.076880285583854)) //Close the polygon.
                .add(LatLng(29.93169018640189, 35.07774736081334)) //Close the polygon.
                .add(LatLng(29.942055989660787, 35.08341609456183)) //Close the polygon.
                .add(LatLng(29.948728098305835, 35.08258365630257)) //Close the polygon.
                .add(LatLng(29.95810435923083, 35.07800524587669)) //Close the polygon.
                .add(LatLng(29.97117558663113, 35.0919931868876)) //Close the polygon.
                .add(LatLng(29.973377819503412, 35.088737374589876)) //Close the polygon.
                .add(LatLng(29.992809442600905, 35.098772412560436)) //Close the polygon.
                .add(LatLng(29.995774842164238, 35.115824420035324)) //Close the polygon.
                .add(LatLng(30.00187764518423, 35.11685022390995)) //Close the polygon.
                .add(LatLng(30.00408337752286, 35.11007733281928)) //Close the polygon.
                .add(LatLng(30.039842096762243, 35.112753342947784)) //Close the polygon.
                .add(LatLng(30.06296640567419, 35.1309056116701)) //Close the polygon.
                .add(LatLng(30.06412440955838, 35.14642647054414)) //Close the polygon.
                .add(LatLng(30.085197709462392, 35.1518230910226)) //Close the polygon.
                .add(LatLng(30.08805345227566, 35.148924080072575)) //Close the polygon.
                .add(LatLng(30.09484515791292, 35.15338409691877)) //Close the polygon.
                .add(LatLng(30.115757593843604, 35.15744271238563)) //Close the polygon.
                .add(LatLng(30.12405189094588, 35.16167972838952)) //Close the polygon.
                .add(LatLng(30.13053253156585, 35.157710313413595)) //Close the polygon.
                .add(LatLng(30.13620274288969, 35.160787725037466)) //Close the polygon.
                .add(LatLng(30.15598807139205, 35.15458830158758)) //Close the polygon.
                .add(LatLng(30.163662013809596, 35.144419463128834)) //Close the polygon.
                .add(LatLng(30.204522044642264, 35.15110233296343)) //Close the polygon.
                .add(LatLng(30.24314796956627, 35.145225489606794)) //Close the polygon.
                .add(LatLng(30.25479936407272, 35.14984989081057)) //Close the polygon.
                .add(LatLng(30.28358902370823, 35.14599622312398)) //Close the polygon.
                .add(LatLng(30.30804527794145, 35.154763317128)) //Close the polygon.
                .add(LatLng(30.347378759865006, 35.191662185576824)) //Close the polygon.
                .add(LatLng(30.404645977105798, 35.16459016997172)) //Close the polygon.
                .add(LatLng(30.442528609248722, 35.16169991917738)) //Close the polygon.
                .add(LatLng(30.499989053911104, 35.192625602719716)) //Close the polygon.
                .add(LatLng(30.53393455361574, 35.19426341157162)) //Close the polygon.
                .add(LatLng(30.549036073997474, 35.20341587246498)) //Close the polygon.
                .add(LatLng(30.582798555785544, 35.203608555883356)) //Close the polygon.
                .add(LatLng(30.624093906661212, 35.22730861241897)) //Close the polygon.
                .add(LatLng(30.66048223195864, 35.26343674749606)) //Close the polygon.
                .add(LatLng(30.6696807561479, 35.266037973333056)) //Close the polygon.
                .add(LatLng(30.673989673284545, 35.26324406426028)) //Close the polygon.
                .add(LatLng(30.68169552658931, 35.270758716249134)) //Close the polygon.
                .add(LatLng(30.685755352919443, 35.26825383235189)) //Close the polygon.
                .add(LatLng(30.691223420073896, 35.27403433388178)) //Close the polygon.
                .add(LatLng(30.711850157609398, 35.28241606115122)) //Close the polygon.
                .add(LatLng(30.710799587482498, 35.29127627495702)) //Close the polygon.
                .add(LatLng(30.714341725397187, 35.294290749953106)) //Close the polygon.
                .add(LatLng(30.719640899043828, 35.29488551938577)) //Close the polygon.
                .add(LatLng(30.724428498495183, 35.28693723696744)) //Close the polygon.
                .add(LatLng(30.736094371818723, 35.294831449550244)) //Close the polygon.
                .add(LatLng(30.76323181386384, 35.29499365942939)) //Close the polygon.
                .add(LatLng(30.763928736458514, 35.30910591596807)) //Close the polygon.
                .add(LatLng(30.77029372963391, 35.3128908125757)) //Close the polygon.
                .add(LatLng(30.791429918604976, 35.31581058983662)) //Close the polygon.
                .add(LatLng(30.7981646510937, 35.322677473286475)) //Close the polygon.
                .add(LatLng(30.79988309376246, 35.33641124038504)) //Close the polygon.
                .add(LatLng(30.81669434198942, 35.34133160590703)) //Close the polygon.
                .add(LatLng(30.82026983575223, 35.33770891936261)) //Close the polygon.
                .add(LatLng(30.842741319955543, 35.33019319647174)) //Close the polygon.
                .add(LatLng(30.863165376375758, 35.33192343489339)) //Close the polygon.
                .add(LatLng(30.910541709782954, 35.35533572290314)) //Close the polygon.
                .add(LatLng(30.9281220622051, 35.37150263767038)) //Close the polygon.
                .add(LatLng(30.927565477896866, 35.39383352636952)) //Close the polygon.
                .add(LatLng(30.932713759267457, 35.39805098265399)) //Close the polygon.
                .add(LatLng(30.93493995719556, 35.40270099821846)) //Close the polygon.
                .add(LatLng(30.950846487947032, 35.416218485533804)) //Close the polygon.
                .add(LatLng(31.00920956488957, 35.41886791325495)) //Close the polygon.
                .add(LatLng(31.044516019470183, 35.42368013879933)) //Close the polygon.
                .add(LatLng(31.084995755254162, 35.44806568585815)) //Close the polygon.
                .add(LatLng(31.122449625188924, 35.45633838816064)) //Close the polygon.
                .add(LatLng(31.159194679352854, 35.4521750020408)) //Close the polygon.
                .add(LatLng(31.178625850742325, 35.440820312764025)) //Close the polygon.
                .add(LatLng(31.19870053936436, 35.42373420879717)) //Close the polygon.
                .add(LatLng(31.227509986490475, 35.40443123687283)) //Close the polygon.
                .add(LatLng(31.243413848728444, 35.39907831179912)) //Close the polygon.
                .add(LatLng(31.269436775482358, 35.40259285846147)) //Close the polygon.
                .add(LatLng(31.315733362256232, 35.4256807268111)) //Close the polygon.
                .add(LatLng(31.35660514208845, 35.453851170334275)) //Close the polygon.
                .add(LatLng(31.42924027710265, 35.476150037353314)) //Close the polygon.
                .add(LatLng(31.578697420952526, 35.48533669760435)) //Close the polygon.
                .add(LatLng(31.721373861363617, 35.53684441720486)) //Close the polygon.
                .add(LatLng(31.7622969190452, 35.558451963883975)) //Close the polygon.
                .add(LatLng(31.76774692832768, 35.55281468284561)) //Close the polygon.
                .add(LatLng(31.770151659665153, 35.55566821674131)) //Close the polygon.
                .add(LatLng(31.772194568155648, 35.55551803074681)) //Close the polygon.
                .add(LatLng(31.77410975399781, 35.5517383498423)) //Close the polygon.
                .add(LatLng(31.783791475010535, 35.547382955943604)) //Close the polygon.
                .add(LatLng(31.7907063695429, 35.55138791577513)) //Close the polygon.
                .add(LatLng(31.796705951262133, 35.551337853782194)) //Close the polygon.
                .add(LatLng(31.800726729447117, 35.5466069949551)) //Close the polygon.
                .add(LatLng(31.803790062260916, 35.54472966996461)) //Close the polygon.
                .add(LatLng(31.805108965863315, 35.54280228303506)) //Close the polygon.
                .add(LatLng(31.810022779658162, 35.54342805801218)) //Close the polygon.
                .add(LatLng(31.811043799076867, 35.54257700404329)) //Close the polygon.
                .add(LatLng(31.811469220650586, 35.54355321297102)) //Close the polygon.
                .add(LatLng(31.81572332715118, 35.544504390936254)) //Close the polygon.
                .add(LatLng(31.816552855088343, 35.54397873995547)) //Close the polygon.
                .add(LatLng(31.818892509123497, 35.54180104303506)) //Close the polygon.
                .add(LatLng(31.818892509123497, 35.540174028094526)) //Close the polygon.
                .add(LatLng(31.81983051678535, 35.54219277119229)) //Close the polygon.
                .add(LatLng(31.822531652547905, 35.542968732163935)) //Close the polygon.
                .add(LatLng(31.82280814230144, 35.54467084010172)) //Close the polygon.
                .add(LatLng(31.823722679433462, 35.548750892952604)) //Close the polygon.
                .add(LatLng(31.827189331056108, 35.546973692030825)) //Close the polygon.
                .add(LatLng(31.836929287096407, 35.54632288607713)) //Close the polygon.
                .add(LatLng(31.842628173314164, 35.5446207781345)) //Close the polygon.
                .add(LatLng(31.843903995251708, 35.548750892987975)) //Close the polygon.
                .add(LatLng(31.847050946919033, 35.54301879419747)) //Close the polygon.
                .add(LatLng(31.846817055555857, 35.5463479170758)) //Close the polygon.
                .add(LatLng(31.849432352491533, 35.54359450717643)) //Close the polygon.
                .add(LatLng(31.848262919938218, 35.541016314270664)) //Close the polygon.
                .add(LatLng(31.850452936057504, 35.54304382519656)) //Close the polygon.
                .add(LatLng(31.856788806602253, 35.53678607534568)) //Close the polygon.
                .add(LatLng(31.8678223870837, 35.54752437403965)) //Close the polygon.
                .add(LatLng(31.872796632949353, 35.549752132958226)) //Close the polygon.
                .add(LatLng(31.875517474476226, 35.54417022014133)) //Close the polygon.
                .add(LatLng(31.882680555615842, 35.53298136342442)) //Close the polygon.
                .add(LatLng(31.88552862894782, 35.53593502127226)) //Close the polygon.
                .add(LatLng(31.890990732114986, 35.530027705423834)) //Close the polygon.
                .add(LatLng(31.894497359142658, 35.534608378293804)) //Close the polygon.
                .add(LatLng(31.900341440636133, 35.527374419523596)) //Close the polygon.
                .add(LatLng(31.907290101976084, 35.52417045156831)) //Close the polygon.
                .add(LatLng(31.91383453868123, 35.526323117482406)) //Close the polygon.
                .add(LatLng(31.92284300168411, 35.526523365494405)) //Close the polygon.
                .add(LatLng(31.925010000299213, 35.53415782024993)) //Close the polygon.
                .add(LatLng(31.924415143034025, 35.535334277206935)) //Close the polygon.
                .add(LatLng(31.924946265776704, 35.5362604241735)) //Close the polygon.
                .add(LatLng(31.929832451239513, 35.53558458723156)) //Close the polygon.
                .add(LatLng(31.936205346185467, 35.54567207986288)) //Close the polygon.
                .add(LatLng(31.936247830780417, 35.54124159310682)) //Close the polygon.
                .add(LatLng(31.93807464501017, 35.54076600412421)) //Close the polygon.
                .add(LatLng(31.941558236624083, 35.5438197860126)) //Close the polygon.
                .add(LatLng(32.03583800997625, 35.518676302540065)) //Close the polygon.
                .add(LatLng(32.08280194792892, 35.546217056632145)) //Close the polygon.
                .add(LatLng(32.10972935092341, 35.53326735612785)) //Close the polygon.
                .add(LatLng(32.211383420057366, 35.5709392129911)) //Close the polygon.
                .add(LatLng(32.64756476556795, 35.56190653324718)) //Close the polygon.
                .add(LatLng(32.681091689457205, 35.660783484784176)) //Close the polygon.
                .add(LatLng(32.74579772599941, 35.75828714532761)) //Close the polygon.
                .add(LatLng(32.75619253152395, 35.795366002153976)) //Close the polygon.
                .add(LatLng(32.737712038648716, 35.79948587513469)) //Close the polygon.
                .add(LatLng(32.72038309532118, 35.936814974491625)) //Close the polygon.
                .add(LatLng(32.72269381571451, 35.935441683498055)) //Close the polygon.
                .add(LatLng(32.682247565926836, 35.96977395833729)) //Close the polygon.
                .add(LatLng(32.67646803398606, 35.95192117542089)) //Close the polygon.
                .add(LatLng(32.666063934135536, 35.96428079436301)) //Close the polygon.
                .add(LatLng(32.65797101893188, 36.02470559808007)) //Close the polygon.
                .add(LatLng(32.51333139456349, 36.076890662915545)) //Close the polygon.
                .add(LatLng(32.5249111501265, 36.15654154054258)) //Close the polygon.
                .add(LatLng(32.53070046835431, 36.20186014333036)) //Close the polygon.
                .add(LatLng(32.37773748772616, 36.39686746441722)) //Close the polygon.
                .add(LatLng(32.318214573349366, 36.841631711082414)) //Close the polygon.
                .fillColor(Color.argb(20, 0, 20, 0))
                .strokeColor(Color.BLACK)
                .strokeWidth(3f)
        //Add the polygon to the map.
        val polygon: Polygon = mMapJordan.addPolygon(polygonOptions)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMapJordan = googleMap

        getLocationAccess()
        switch1?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                Toast.makeText(this, "MAP TYPE TERRAIN", Toast.LENGTH_SHORT).show()
            } else {
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                Toast.makeText(this, "MAP TYPE HYBRID", Toast.LENGTH_SHORT).show()
            }
        })
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.isTrafficEnabled = true
        googleMap.setOnMyLocationChangeListener { }
        //googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.isBuildingsEnabled = true
        googleMap.uiSettings.setAllGesturesEnabled(true)
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.getUiSettings().setZoomControlsEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

}
//    in youtube :
//    Polylines and Polygons to Represent Routes and Areas