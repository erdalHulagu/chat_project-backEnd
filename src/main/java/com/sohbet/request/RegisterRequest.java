package com.sohbet.request;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.sohbet.domain.Image;
import com.sohbet.domain.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	
	@Size(max=50)
	@NotBlank(message="Please provide yout First Name")
	private String firstName;
	
	@Size(max=50)
	@NotBlank(message="Please provide yout Last Name")
	private String lastName;
	
	@Size(min=5, max=80)
	@Email(message="Please provide valid e-mail")
	private String email;
	
	@Size(min=4, max=20, message="Please provide Correct Size of Password")
	@NotBlank(message="Please provide your password")
	private String password;
	
//	 @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
//	            message = "Please provide valid phone number")
//	@Size(min=14, max=14)
//    @NotBlank(message = "Please provide your phone number")
//	private String phoneNumber;
	
    @Size(max= 100)
    @NotBlank(message = "Please provide your address")
	private String address;

	
    @CreationTimestamp
    @Column(name = "creatAt", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updateAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // CascadeType.ALL: Eşleşen resim verisini silerken kullanıcıyı da siler
    private Image profileImage;// burayi Set<String> yapma ihtimalin var yani burada bir islem yapacaksin register icin

    
    @ManyToMany   // hibernate defaultta LAZY
    @JoinTable( name="t_user_role",
    						 joinColumns = @JoinColumn(name="user_id"),
    						 inverseJoinColumns = @JoinColumn(name="role_id"))
    private  Set<Role> roles = new HashSet<>();


}