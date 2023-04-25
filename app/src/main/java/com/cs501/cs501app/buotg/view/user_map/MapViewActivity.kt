package com.cs501.cs501app.buotg.view.user_map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.cs501.cs501app.utils.GenericTopAppBar
import com.cs501.cs501app.utils.TAlert
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

fun launchMap(ctx: Context, from: Location, to: Location) {
    val mapUrl =
        "https://www.google.com/maps/dir/${from.latitude},${from.longitude}/${to.latitude},${to.longitude}"
    launchURIView(ctx, mapUrl)
}

fun launchMap(ctx: Context, from: String, to: String) {
    val uri = "http://maps.google.com/maps?f=d&saddr=$from,&daddr=$to&hl=en"
    launchURIView(ctx, uri)
}

fun launchMap(ctx: Context, from: Location, to: String) {
    val uri = "https://www.google.com/maps/dir/${from.latitude},${from.longitude}/$to"
    launchURIView(ctx, uri)
}

private fun launchURIView(ctx: Context, uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    startActivity(ctx, intent, null)
}

const val MAPVIEW_KEY = "MAPVIEW_KEY"

class MapViewActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        savedInstanceState?.let{ itOut ->
            itOut.getString(MAPVIEW_KEY)?.let{
                handleIconClick(this, it)
            }
        }
        setContent {
            MaterialTheme {
                RenderScaffold()
            }
        }
    }

    private fun handleIconClick(ctx: Context, addrString: String,) {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            TAlert.fail(ctx, "Permission Denied" + " Please allow location permission")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                launchMap(ctx, location, addrString)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RenderScaffold() {
        val ctx = LocalContext.current
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                GenericTopAppBar(title = "Map View",                            onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }, finished = {finish()})
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                LazyColumn() {
                    items(buildings.size) { index ->
                        val building = buildings[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column() {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = building.code + " " + building.name,
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(text = building.address)
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = "Location",
                                        modifier = Modifier.clickable {
                                            handleIconClick(ctx, "Boston MA ${building.address}")
                                        })
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}

data class Building(val code: String, val name: String, val address: String)

const val buildingCodes = "AAS\tAfrican American Studies\t138 Mountfort Street\n" +
        "AGG\tAgganis Arena\t925 Commonwealth Avenue\n" +
        "ASC\tAcademic Support Center\t300 Babcock Street\n" +
        "BRB\tBiology Research Building\t5 Cummington Mall\n" +
        "BSC\tBiological Science Center\t2 Cummington Mall\n" +
        "CAS\tCollege of Arts & Sciences\t685–725 Commonwealth Avenue\n" +
        "CDS\tFaculty of Computing & Data Sciences\t645–655 Commonwealth Avenue\n" +
        "CFA\tCollege of Fine Arts\t855 Commonwealth Avenue\n" +
        "CGS\tCollege of General Studies\t871 Commonwealth Avenue\n" +
        "CLN\tClinical Psychology\t900 Commonwealth Avenue\n" +
        "CNS\tCognitive & Neural Systems\t677 Beacon Street\n" +
        "COM\tCollege of Communication\t640 Commonwealth Avenue\n" +
        "CRW\tDeWolfe Crew Boathouse\t619 Memorial Drive\n" +
        "CSE\tCase Physical Education Center\t285 Babcock Street\n" +
        "EGL\tEnglish Faculty offices\t236 Bay State Road\n" +
        "EIB\tEditorial Institute Building\t143 Bay State Road\n" +
        "EIL\tEilberg Lounge (Case Center)\t285 Babcock Street\n" +
        "EMA\tEngineering Manufacturing Annex\t730 Commonwealth Avenue\n" +
        "EMB\tEngineering Manufacturing Building\t15 St. Mary’s Street\n" +
        "ENG\tCollege of Engineering\t110–112 Cummington Mall\n" +
        "EOP\tCenter for English Language & Orientation Programs\t890 Commonwealth Avenue\n" +
        "EPC\tEngineering Product Innovation Center (EPIC)\t750 Commonwealth Avenue\n" +
        "ERA\tEngineering Research Annex\t48 Cummington Mall\n" +
        "ERB\tEngineering Research Building\t44 Cummington Mall\n" +
        "FAB\tFenway Activities Building\t180 Riverway\n" +
        "FCB\tFenway Classroom Building\t25 Pilgrim Road\n" +
        "FCC\tFenway Campus Center\t150 Riverway\n" +
        "FLR\tFuller Building\t808 Commonwealth Avenue\n" +
        "FOB\tFaculty Office Building (Alden Hall)\t704 Commonwealth Avenue\n" +
        "FRC\tFitness & Recreation Center\t915 Commonwealth Avenue\n" +
        "GDP\tGlobal Development Policy Center\t53 Bay State Road\n" +
        "GMS\tGraduate Medical Sciences\t72 East Concord Street\n" +
        "GRS\tGraduate School of Arts & Sciences\t705 Commonwealth Avenue\n" +
        "GSU\tGeorge Sherman Union\t775 Commonwealth Avenue\n" +
        "HAR\tRafik B. Hariri Building (Questrom School of Business)\t595 Commonwealth Avenue\n" +
        "HAW\tHawes Building\t43 Hawes Street, Brookline\n" +
        "HIS\tHistory and American Studies Departments\t226 Bay State Road\n" +
        "IEC\tInternational Education Center\t888 Commonwealth Avenue\n" +
        "IRB\tInternational Relations Building\t154 Bay State Road\n" +
        "IRC\tInternational Relations Center\t152 Bay State Road\n" +
        "JSC\tElie Wiesel Center for Jewish Studies\t147 Bay State Road\n" +
        "KCB\tKenmore Classroom Building\t565 Commonwealth Avenue\n" +
        "KHC\tKilachand Honors College\t91 Bay State Road\n" +
        "LAW\tLaw School\t765 Commonwealth Avenue\n" +
        "LEV\tLeventhal Center\t233 Bay State Road\n" +
        "LNG\tRomance Studies, Modern Foreign Languages & Comparative Literature\t718 Commonwealth Avenue\n" +
        "LSE\tLife Science & Engineering Building\t24 Cummington Mall\n" +
        "MCH\tMarsh Chapel\t735 Commonwealth Avenue\n" +
        "MCS\tMath & Computer Science\t111 Cummington Mall\n" +
        "MED\tAram V. Chobanian & Edward Avedisian School of Medicine\t715 Albany Street\n" +
        "MET\tMetropolitan College\t1010 Commonwealth Avenue\n" +
        "MOR\tMorse Auditorium\t602 Commonwealth Avenue\n" +
        "MUG\tMugar Memorial Library\t771 Commonwealth Avenue\n" +
        "PDP\tPhysical Development Program\t915 Commonwealth Avenue\n" +
        "PHO\tPhotonics Building\t6–8 St. Mary’s Street\n" +
        "PLS\tAnthropology, Philosophy, Political Science\t232 Bay State Road\n" +
        "PRB\tPhysics Research Building\t3 Cummington Mall\n" +
        "PSY\tPsychological & Brain Sciences\t64–72–86 Cummington Mall\n" +
        "PTH\tPlaywrights’ Theatre\t949 Commonwealth Avenue\n" +
        "REL\tCAS Religion\t145 Bay State Road\n" +
        "RKC\tRajen Kilachand Center for Integrated Life Sciences & Engineering\t610 Commonwealth Avenue\n" +
        "SAC\tSargent Activities Center\t1 University Road\n" +
        "SAL\tSailing Docks\tCharles River Behind BU Bridge\n" +
        "SAR\tCollege of Health & Rehabilitation Sciences: Sargent College\t635 Commonwealth Avenue\n" +
        "SCI\tMetcalf Science Center\t590–596 Commonwealth Avenue\n" +
        "SDM\tHenry M. Goldman School of Dental Medicine\t100 East Newton Street\n" +
        "SHA\tSchool of Hospitality Administration\t928 Commonwealth Avenue\n" +
        "SLB\tScience Library Building\t30–38 Cummington Mall\n" +
        "SOC\tSociology\t96–100 Cummington Mall\n" +
        "SPH\tSchool of Public Health\t715 Albany Street\n" +
        "SSW\tSchool of Social Work\t264–270 Bay State Road\n" +
        "STH\tSchool of Theology\t745 Commonwealth Avenue\n" +
        "STO\tStone Science Building\t675 Commonwealth Avenue\n" +
        "THA\tJoan & Edgar Booth Theatre\t820 Commonwealth Avenue\n" +
        "TTC\tTrack & Tennis Center\t100 Ashford Street\n" +
        "WEA\tWheelock College of Education & Human Development Annex\t621 Commonwealth Avenue\n" +
        "WED\tWheelock College of Education & Human Development\t2 Silber Way\n" +
        "YAW\tYawkey Center for Student Services\t100 Bay State Road"
val buildings = buildingCodes.split("\n").map {
    val (code, name, address) = it.split("\t")
    Building(code, name, address)
}