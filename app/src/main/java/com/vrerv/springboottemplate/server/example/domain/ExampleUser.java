package com.vrerv.springboottemplate.server.example.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

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
public class ExampleUser extends EntityId {

	@Setter(AccessLevel.PUBLIC)
	@NotNull
	@Length(min = 2, max = 32)
	@Comment("사용자 이름")
	private String name;

	@Setter(AccessLevel.PUBLIC)
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	private List<ExampleContact> contacts;

}
