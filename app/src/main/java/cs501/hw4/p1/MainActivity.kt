package cs501.hw4.p1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import cs501.hw4.p1.ui.theme.P1Theme

//Build a multi-pane shopping app where users can browse a list of products on one side and
//view detailed information about the selected product on the other pane.
//The app should work in both portrait and landscape orientations, dynamically adjusting the layout.
//In portrait mode, the app will show only one pane at a time (either the product list or product details),
//while in landscape mode, both panes will be displayed side by side.
//
//Features:
//
//Product List:
//• Display a list of products (e.g., “Product A”, “Product B”, “Product C”) in a LazyColumn.
//• Each product should be clickable. When clicked, it shows the product details in a second pane.
//Product Details:
//• When a product is selected, display its details in another pane (name, price, description).
//• If no product is selected, display a placeholder message like “Select a product to view details.”
//Multi-Pane Layout:
//• In portrait mode, display only the product list first. When a product is selected, navigate to a second screen to display product details.
//• In landscape mode, use a Row layout to show both the product list and the product details side by side.
//State Management:
//• Use remember and mutableStateOf to track the selected product and update the UI accordingly.
//Instructions:
//
//Step 1: Create the Product List:
//• Use a LazyColumn to display a list of products (e.g., “Product A”, “Product B”, “Product C”).
//• Use Text to display the product names.
//• Make each product clickable using the clickable modifier. When a product is clicked, update the selected product’s state.
//Step 2: Create the Product Details Pane:
//• When a product is selected from the list, display the product’s details (name, price, and description) in another pane.
//• If no product is selected, display a placeholder message.
//Step 3: Handle Configuration Changes:
//• In portrait mode, the app should first show the product list. When a product is clicked, navigate to another screen that shows the product details.
//• In landscape mode, use a Row layout to show both the product list and the product details side by side.
//Products can be hardcoded:
//
//val products = listOf(
//    Product("Product A", "$100", "This is a great product A."),
//    Product("Product B", "$150", "This is product B with more features."),
//    Product("Product C", "$200", "Premium product C.")
//    …
//)
//
//You are to handle the following:
//•	Dynamic layouts
//•	State management
//•	Multi-pane layout
//•	Handling user input
data class Product(
    val name: String,
    val price: String,
    val description: String
)

val products = listOf(
    Product("Product A", "$100", "This is a great product A."),
    Product("Product B", "$150", "This is product B with more features."),
    Product("Product C", "$200", "Premium product C."),
    Product("Product D", "$250", "Description of product D."),
    Product("Product E", "$300", "Description of product E."),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            P1Theme {
                ShoppingApp()
            }
        }
    }
}

@Composable
fun ShoppingApp() {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val isLandscape = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row{
            ProductList(onProductSelected = { selectedProduct = it })

            ProductDetail(Modifier.weight(1f), selectedProduct)
        }
    } else {
        if (selectedProduct == null) {
            ProductList { selectedProduct = it }
        } else {
            ProductDetail(selectedProduct = selectedProduct, onBack = { selectedProduct = null })
        }
    }
}

@Composable
fun ProductList(onProductSelected: (Product) -> Unit) {
    LazyColumn{
        items(products) { product ->
            Box(modifier = Modifier.clickable { onProductSelected(product) }.padding(19.dp)) {
                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ProductDetail(modifier: Modifier = Modifier, selectedProduct: Product?, onBack: (() -> Unit)? = null) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (selectedProduct != null) {
            Column {
                onBack?.let {
                    Button(onClick = onBack, modifier = Modifier.padding(bottom = 16.dp)) {
                        Text("Back")
                    }
                }
                Text(
                    text = selectedProduct.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = selectedProduct.price,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = selectedProduct.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            Text(
                text = "Select a product to view details",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}