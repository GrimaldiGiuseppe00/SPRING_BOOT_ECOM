package com.ecommerce.Ecom.service;

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

import java.util.List;
import java.util.stream.Stream;

public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {
//    FIND EXISTING CART OR CREATE ONE
        Cart userCart = createCart();
//        RETRIEVE PRODUCT DETAILS
        Product product = productRepository.findById(productId)
            .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId)) ;
//        PERFORM VALIDATIONS
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                userCart.getCartId(),
                productId
        );
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }
        if (product.getQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please make an order of the " + product.getProductName() + " less than o equal to the quantity " + product.getQuantity() + "." );
        }
//        CREATE CART ITEM
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(userCart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
//        SAVE CART ITEM
        CartItem savedCartItem = cartItemRepository.save(newCartItem);
//        RETURN UPDATED CART
        product.setQuantity(product.getQuantity() - quantity);
        userCart.setTotalPrice(userCart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(userCart);
        CartDto cartDto = modelMapper.map(userCart, CartDto.class);
        List<CartItem> cartItems = userCart.getCartItems();
        Stream<ProductDto> productStream = cartItems.stream().map(item ->{
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
            cartDto.setProducts(productStream.toList());
            return cartDto;

    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;


    }
}
