package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.utils.AuthUtil;
import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Cart;
import com.ecommerce.Ecom.model.CartItem;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.CartDto;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.repositories.CartItemRepository;
import com.ecommerce.Ecom.repositories.CartRepository;
import com.ecommerce.Ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {
        // Trova o crea il carrello dell’utente
        Cart userCart = createCart();

        // Recupera il prodotto
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Controlla se il prodotto è già nel carrello
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                userCart.getCartId(),
                productId
        );
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        // Controlla disponibilità
        if (product.getQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please order quantity less than or equal to " + product.getQuantity());
        }

        // Crea CartItem e salva
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(userCart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        // Aggiorna quantità prodotto e prezzo totale carrello
        product.setQuantity(product.getQuantity() - quantity);
        userCart.setTotalPrice(userCart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(userCart);

        // Mappa in DTO
        CartDto cartDto = modelMapper.map(userCart, CartDto.class);
        List<CartItem> cartItems = userCart.getCartItems();
        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDto.setProducts(productStream.toList());

        return cartDto;
    }

    @Override
    public CartDto getCart() {
        String authUserEmail = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(authUserEmail);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", authUserEmail);
        }
        Long cartId = cart.getCartId();
        Cart authUserCart = cartRepository.findCartByEmailAndCartId(authUserEmail, cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        return modelMapper.map(authUserCart, CartDto.class);
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No carts exist");
        }

        return carts.stream().map(cart -> {
            CartDto cartDto = modelMapper.map(cart, CartDto.class);
            List<ProductDto> products = cart.getCartItems()
                    .stream()
                    .map(item -> {
                        ProductDto pdto = modelMapper.map(item.getProduct(), ProductDto.class);
                        pdto.setQuantity(item.getQuantity());
                        return pdto;
                    })
                    .collect(Collectors.toList());
            cartDto.setProducts(products);
            return cartDto;
        }).collect(Collectors.toList());
    }

    // Metodo helper per creare un carrello se non esiste
    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}