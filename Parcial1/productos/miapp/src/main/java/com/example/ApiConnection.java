package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ApiConnection {
    private static final String API_URL = "https://fakestoreapi.com/products/";
    private static HttpClient client = HttpClient.newHttpClient();
    private static Gson gson = new Gson();
    private static ArrayList<Product> productosLocales = new ArrayList<>();

    public static void inicializarProductos() {
        if (productosLocales.isEmpty()) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .GET()
                        .build();
    
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
                if (response.statusCode() == 200) {
                    productosLocales = gson.fromJson(response.body(), new TypeToken<ArrayList<Product>>() {}.getType());
                } else {
                    System.out.println("Error al obtener los productos. Código de estado: " + response.statusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Product imprimirPorID(int id) {
        for (Product producto : productosLocales) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null; 
    }
    public static void AgregarProducto(Product producto) {
        productosLocales.add(producto);
        System.out.println("Producto agregado localmente.");
    }

    public static boolean EliminarProducto(int id) {
        Product productoAEliminar = imprimirPorID(id);
        if (productoAEliminar != null) {
            productosLocales.remove(productoAEliminar);
            System.out.println("Producto eliminado localmente.");
            return true;
        } else {
            System.out.println("No se encontró el producto con el ID proporcionado.");
            return false;
        }
    }

    public static Set<String> obtenerCategoriasLocales() {
        return productosLocales.stream()
                .map(Product::getCategory)
                .collect(Collectors.toSet());
    }
    
    public static ArrayList<Product> buscarProductosPorCategoria(String categoria) {
        return productosLocales.stream()
                .filter(producto -> producto.getCategory().equalsIgnoreCase(categoria))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public static ArrayList<Product> buscarProductosPorNombre(String nombre) {
        // Filtrar los productos cuyo título contenga el nombre ingresado (ignorar mayúsculas/minúsculas)
        return productosLocales.stream()
                .filter(producto -> producto.getTitle().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}


