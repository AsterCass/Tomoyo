//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import biz.DataSource
//import biz.OrderSummaryScreen
//import biz.OrderViewModel
//import biz.SelectOptionScreen
//import biz.StartOrderScreen
//import kotlinx.coroutines.launch
//import org.jetbrains.compose.resources.StringResource
//import org.jetbrains.compose.resources.stringResource
//import org.jetbrains.compose.ui.tooling.preview.Preview
//import tomoyo.composeapp.generated.resources.Res
//import tomoyo.composeapp.generated.resources.app_name
//import tomoyo.composeapp.generated.resources.back_button
//import tomoyo.composeapp.generated.resources.choose_flavor
//import tomoyo.composeapp.generated.resources.choose_pickup_date
//import tomoyo.composeapp.generated.resources.order_summary
//
//enum class CupcakeScreen(val title: StringResource) {
//    Start(title = Res.string.app_name),
//    Flavor(title = Res.string.choose_flavor),
//    Pickup(title = Res.string.choose_pickup_date),
//    Summary(title = Res.string.order_summary)
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CupcakeAppBar(
//    currentScreen: CupcakeScreen,
//    canNavigateBack: Boolean,
//    navigateUp: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    TopAppBar(
//        title = { Text(stringResource(currentScreen.title)) },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        ),
//        modifier = modifier,
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = stringResource(Res.string.back_button)
//                    )
//                }
//            }
//        }
//    )
//}
//
//
//@Composable
//@Preview
//fun App(
//    viewModel: OrderViewModel = viewModel { OrderViewModel() },
//    navController: NavHostController = rememberNavController(),
//) {
//    MaterialTheme {
//        // Get current back stack entry
//        val backStackEntry by navController.currentBackStackEntryAsState()
//        // Get the name of the current screen
//        val currentScreen = CupcakeScreen.valueOf(
//            backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
//        )
//
//
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//
//
//                val greeting = remember { Greeting().greet() }
//                var someText by remember { mutableStateOf("") }
//
//                val scope = rememberCoroutineScope()
//                LaunchedEffect(Unit) {
//                    scope.launch {
//                        someText = Greeting().greeting()
//                    }
//                }
//
//                Column(
//                    modifier = Modifier.padding(all = 20.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                ) {
//                    greeting.forEach { greeting ->
//                        Text(someText)
//                        Divider()
//                    }
//                }
//
//
//            }
//        }
//
//
//        Scaffold(
//            topBar = {
//                CupcakeAppBar(
//                    currentScreen = currentScreen,
//                    canNavigateBack = navController.previousBackStackEntry != null,
//                    navigateUp = { navController.navigateUp() }
//                )
//            }
//        ) { innerPadding ->
//            val uiState by viewModel.uiState.collectAsState()
//
//            NavHost(
//                navController = navController,
//                startDestination = CupcakeScreen.Start.name,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(innerPadding)
//            ) {
//                composable(route = CupcakeScreen.Start.name) {
//                    StartOrderScreen(
//                        quantityOptions = DataSource.quantityOptions,
//                        onNextButtonClicked = {
//                            viewModel.setQuantity(it)
//                            navController.navigate(CupcakeScreen.Flavor.name)
//                        },
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
//                    )
//                }
//                composable(route = CupcakeScreen.Flavor.name) {
//                    SelectOptionScreen(
//                        subtotal = uiState.price,
//                        onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
//                        onCancelButtonClicked = {
//                            cancelOrderAndNavigateToStart(viewModel, navController)
//                        },
//                        options = DataSource.flavors.map { id -> stringResource(id) },
//                        onSelectionChanged = { viewModel.setFlavor(it) },
//                        modifier = Modifier.fillMaxHeight()
//                    )
//                }
//                composable(route = CupcakeScreen.Pickup.name) {
//                    SelectOptionScreen(
//                        subtotal = uiState.price,
//                        onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
//                        onCancelButtonClicked = {
//                            cancelOrderAndNavigateToStart(viewModel, navController)
//                        },
//                        options = uiState.pickupOptions,
//                        onSelectionChanged = { viewModel.setDate(it) },
//                        modifier = Modifier.fillMaxHeight()
//                    )
//                }
//                composable(route = CupcakeScreen.Summary.name) {
//                    OrderSummaryScreen(
//                        orderUiState = uiState,
//                        onCancelButtonClicked = {
//                            cancelOrderAndNavigateToStart(viewModel, navController)
//                        },
//                        onSendButtonClicked = { subject: String, summary: String ->
//                            shareOrder(subject = subject, summary = summary)
//                        },
//                        modifier = Modifier.fillMaxHeight()
//                    )
//                }
//            }
//        }
//
//
//    }
//}
//
//private fun cancelOrderAndNavigateToStart(
//    viewModel: OrderViewModel,
//    navController: NavHostController
//) {
//    viewModel.resetOrder()
//    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
//}
//
//private fun shareOrder(subject: String, summary: String) {
//    // TODO
//}