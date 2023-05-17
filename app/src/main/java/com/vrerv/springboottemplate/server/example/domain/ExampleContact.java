package com.vrerv.springboottemplate.server.example.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.vrerv.springboottemplate.server.common.lib.entity.EntityId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

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
public class ExampleContact extends EntityId {

	public enum ExampleContactType {
		MOBILE, LANDLINE, EMAIL
	}

	@Setter(AccessLevel.PUBLIC)
	@ManyToOne(optional = false)
	private ExampleUser user;

	@NotBlank
	@Comment("연락처 이름")
	private String name;

	@Default
	@NotNull
	@Enumerated(EnumType.STRING)
	@Comment("연락처 타입")
	private ExampleContactType contactType = ExampleContactType.MOBILE;

	@NotBlank
	private String value;

}
