package com.vrerv.springboottemplate.server.feature.user;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.vrerv.springboottemplate.server.common.infra.security.PasswordEncodingEntityListener;
import com.vrerv.springboottemplate.server.common.infra.security.PasswordEncodingEntityListener.PasswordEncodable;
import com.vrerv.springboottemplate.server.common.lib.entity.EntityId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

// Using builder rather using constructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
// only open setters public on each updatable field
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@SuperBuilder
@DynamicUpdate
@Entity
@SuppressWarnings("JpaObjectClassSignatureInspection")
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"account"})
})
@EntityListeners(PasswordEncodingEntityListener.class)
public class User extends EntityId implements PasswordEncodable {

	@Comment("사용자 계정")
	@NotBlank
	@Length(min = 2, max = 32)
	private String account;

	@Comment("암호화된 사용자 인증 암호")
	@NotBlank
	@Setter(AccessLevel.PUBLIC)
	private String password;
}
