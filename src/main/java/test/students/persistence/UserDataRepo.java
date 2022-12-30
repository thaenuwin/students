/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.students.persistence.entity.Users;


/**
 *
 * @author thaenuwin
 */

@Repository
public interface UserDataRepo extends JpaRepository<Users,String> {

    Users findByUserId(String userId);

    Users findByEmail(String email);
}
