package com.sohbet.domain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.sohbet.request.AdminUserUpdateRequest;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
@Entity
//public class Chat {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//	
//	@Column(length = 100, nullable = true)
//	private String chatName;
//	
//	@Column(name = "is_group")
//	private Boolean isGroup;
//	
//	@JoinColumn(name = "createdBy")
//	@ManyToOne
//	private User createdBy;
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "chat_admins",
//        joinColumns = @JoinColumn(name = "chat_id"),
//        inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//	private Set<User> admins = new HashSet<>();
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "chat_users",
//        joinColumns = @JoinColumn(name = "chat_id"),
//        inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//	private Set<User> users = new HashSet<>();
//	
//	@OneToMany(orphanRemoval = true, mappedBy = "chat",fetch = FetchType.EAGER)
//	private List<Message> messages = new ArrayList<>();
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "chat_images",
//        joinColumns = @JoinColumn(name = "chat_id"),
//        inverseJoinColumns = @JoinColumn(name = "image_id")
//    )
//	private Set<Image> chatImage = new HashSet<>();
////	private Set<String> chatImage = new HashSet<>();
//}
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = true)
    private String chatName;

    @Column(name = "is_group")
    private Boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY) // LAZY yükleme ile performans iyileştirme
    @JoinTable(
        name = "chat_admins",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> admins = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY) // LAZY yükleme ile performans iyileştirme
    @JoinTable(
        name = "chat_users",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "chat", fetch = FetchType.LAZY) // LAZY yükleme ve orphanRemoval korundu
    private List<Message> messages = new ArrayList<>();

    @ElementCollection // Image yerine sadece görüntü URL'lerini saklamak için basit bir koleksiyon
    @CollectionTable(name = "chat_images", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "image_url")
    private Set<String> chatImage = new HashSet<>();
}
	

	