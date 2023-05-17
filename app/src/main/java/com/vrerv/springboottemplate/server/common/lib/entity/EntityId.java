package com.vrerv.springboottemplate.server.common.lib.entity;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 */
@Slf4j
@TypeDefs({
		@TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Getter
@Setter(AccessLevel.PACKAGE)
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class EntityId implements Serializable {

	@Serial
	private static final long serialVersionUID = -2907295926386574059L;

	public static <T extends EntityId> T of(Long id, Class<T> type) {
		try {
			Constructor<?>[] constructors = type.getConstructors();
			log.info("constructors: {}", (Object) constructors);
			Constructor<T> constructor = (Constructor<T>) constructors[0];
			T instance = constructor.newInstance();
			instance.setId(id);
			return instance;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EntityId domain) || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		return id != null && Objects.equals(id, domain.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
