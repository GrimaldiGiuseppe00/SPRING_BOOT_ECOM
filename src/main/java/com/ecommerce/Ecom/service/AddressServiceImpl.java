package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.APIException;
import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Address;
import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.payload.AddressDto;
import com.ecommerce.Ecom.repositories.AddressRepository;
import com.ecommerce.Ecom.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivilegedAction;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AddressDto createAddress(AddressDto addressDto , User user) {
//        DTO -> ENTITY
        Address address=modelMapper.map(addressDto, Address.class);
//        OTTENGO INDIRIZZI UTENTE IN SESS
        List<Address> addresses = user.getAddresses();
//        AGGIUNGO NUOVO INDIRIZZO ALLA LISTA
        addresses.add(address);
//        IMPOSTO LA LISTA ALL'UTENTE
        user.setAddresses(addresses);
//        SETTER PER SAVE NEL DB CON NUOVI DATI
        address.setUser(user);
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setStreet(addressDto.getStreet());
        address.setBuildingName(addressDto.getBuildingName());
        address.setPinCode(addressDto.getPinCode());
        Address savedAddress=addressRepository.save(address);
//        RETURN ENTITY -> DTO
        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses= addressRepository.findAll();
        if(addresses.isEmpty()){
            throw new APIException("No address found");
        }
        List<AddressDto> addressDtos=addresses.stream().map(address ->  {
            AddressDto addressDto=modelMapper.map(address, AddressDto.class);
            return addressDto;
        }).collect(Collectors.toList());

        return addressDtos;


    }

    @Override
    public AddressDto getAddressById(Long addressId) {
        Address addressByDb = addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("address", "addressId", addressId));

        AddressDto addressDto=modelMapper.map(addressByDb, AddressDto.class);
        return addressDto;
    }

    @Override
    public List<AddressDto> getAllUserAddresses(User user) {
        List<Address> addresses= user.getAddresses();
        if(addresses.isEmpty()){
            throw new APIException("No address found");
        }
        List<AddressDto>addressDtos=addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();

        return addressDtos;
    }

    @Override
    public AddressDto updateAddressById(Long addressId, AddressDto addressDto) {

        Address addressByDb= addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("address", "addressId", addressId));

        addressByDb.setCountry(addressDto.getCountry());
        addressByDb.setCity(addressDto.getCity());
        addressByDb.setStreet(addressDto.getStreet());
        addressByDb.setPinCode(addressDto.getPinCode());
        addressByDb.setBuildingName(addressDto.getBuildingName());
        Address updatedAddress=addressRepository.save(addressByDb);

        User user = addressByDb.getUser();
        user.getAddresses().removeIf( address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDto.class);

    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address addressByDb= addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("address", "addressId", addressId));
        User user = addressByDb.getUser();
        user.getAddresses().removeIf( address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressByDb);
        return "Address deleted successfully with addressId: " + addressId;





    }

}
