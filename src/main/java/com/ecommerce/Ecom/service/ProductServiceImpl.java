package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Category;
import com.ecommerce.Ecom.model.Product;
import com.ecommerce.Ecom.payload.ProductDto;
import com.ecommerce.Ecom.payload.ProductResponse;
import com.ecommerce.Ecom.repositories.CategoryRepository;
import com.ecommerce.Ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) {
    Category category = categoryRepository.findById(categoryId).orElseThrow(
            ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
    );
    Product product = modelMapper.map(productDto, Product.class);
    product.setImage("default.png");
    product.setCategory(category);
    double specialPrice = product.getPrice()
            -((product.getDiscount() * 0.01) * product.getPrice());
    product.setSpecialPrice(specialPrice);
    Product savedProduct = productRepository.save(product);
    return modelMapper.map(savedProduct, ProductDto.class);

    }

    @Override
    public ProductResponse getAllProducts() {
       List<Product> products = productRepository.findAll();
       List<ProductDto> productDtos = products.stream()
               .map(product-> modelMapper.map(product,ProductDto.class))
               .collect(Collectors.toList());
       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDtos);
       return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        List<Product>products=productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDto> productDtos = products.stream()
                .map(product-> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;



    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
        List<ProductDto> productDtos = products.stream()
                .map(product-> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {

        Product productFromDb = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","ProductId",productId)
        );
        Product product = modelMapper.map(productDto, Product.class);
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setPrice(product.getPrice());
        double specialPrice = product.getPrice()
                -((product.getDiscount() * 0.01) * product.getPrice());
        productFromDb.setSpecialPrice(specialPrice);
        productFromDb.setCategory(product.getCategory());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());

        Product savedProduct=productRepository.save(productFromDb);
        return  modelMapper.map(savedProduct,ProductDto.class);


    }

    @Override
    public ProductDto deleteProduct(Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","ProductId",productId)
        );
      productRepository.delete(productFromDb);
      return modelMapper.map(productFromDb,ProductDto.class);


    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
//        OTTENIAMO PRODOTTO DA DB
        Product productFromDb = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product","ProductId",productId)
        );
//        UPLOAD IMMAGINE SU SERVER

//        OTTENIAMO NOME FILE DEL IMMAGINE CARICATA

        String fileName= fileService.uploadImage(path,image);

//        MODIFICHIAMO IL FILE IMMAGINE DEL PRODOTTO
        productFromDb.setImage(fileName);

//        SALVIAMO IL PRODOTTO MODIFICATO SU DB
        Product updatedProduct=productRepository.save(productFromDb);

//        RITORNIAMO COPIA DI PRODUCT(PRODOTTO MODIFICATO E SALVATO)->PRODUCTDTO
        return modelMapper.map(updatedProduct,ProductDto.class);

    }
    private String uploadImage(String path,MultipartFile file) throws IOException {
//        NOME DEL FILE CORRENTE
        String originalFilename = file.getOriginalFilename();
//        GENERIAMO UN IDENTIFICATIVO UNICO PER IL FILE(UUID)
        String randomId= UUID.randomUUID().toString();
//        CONCATENIAMO LO UUID UNIVOCO A PARTIRE DAL INDICE CHE CONTIENE IL . CIOè LA PARTE FORMATO(ESEMPIO 1234.JPG)
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
//        CONTROLLIAMO CHE IL FILE ESISTA E CREIAMOLO
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
//        CARICHIAMO FILE SUL SERVER
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

}
