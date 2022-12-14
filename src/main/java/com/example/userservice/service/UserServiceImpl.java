package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.repository.UserEntity;
import com.example.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * UserDetailsService callback Method
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // email 로 user 검색
        UserEntity userEntity = userRepository.findByEmail(email);

        // user 를 찾을 수 없는 경우
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // User(UserDetails) 설정 후 리턴
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    /**
     * 회원 저장
     * userId(UUID 사용)
     * password -> encrypted_password
     * userDTO to userEntity
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // userId 설정
        userDTO.setUserId(UUID.randomUUID().toString());

        // Spring-Security BCryptPassword
        userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        // UserDTO to userEntity
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);

        // save
        userRepository.save(userEntity);

        return userDTO;
    }

    @Override
    public UserDTO getUserByEmail(String email) {

        // email 로 User 검색
        UserEntity userEntity = userRepository.findByEmail(email);

        // UserEntity to UserDTO
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        return mapper.map(userEntity, UserDTO.class);
    }
}
