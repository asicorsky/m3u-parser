package com.asikorsky.m3u.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "channels")
@Entity
public class ChannelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int history;

	private String url;

	private String status;

	private BigDecimal connectionCoefficient;

	@ManyToMany(mappedBy = "channels")
	private List<CategoryEntity> categories;

	@ManyToMany(mappedBy = "channels")
	private List<TemplateEntity> templates;

	public ChannelEntity() {

	}

	public ChannelEntity(String name, int history, String url, String status, BigDecimal connectionCoefficient) {

		this.name = name;
		this.history = history;
		this.url = url;
		this.status = status;
		this.connectionCoefficient = connectionCoefficient;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public int getHistory() {

		return history;
	}

	public void setHistory(int history) {

		this.history = history;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public List<CategoryEntity> getCategories() {

		return categories;
	}

	public void setCategories(List<CategoryEntity> categories) {

		this.categories = categories;
	}

	public List<TemplateEntity> getTemplates() {

		return templates;
	}

	public void setTemplates(List<TemplateEntity> templates) {

		this.templates = templates;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public BigDecimal getConnectionCoefficient() {

		return connectionCoefficient;
	}

	public void setConnectionCoefficient(BigDecimal connectionCoefficient) {

		this.connectionCoefficient = connectionCoefficient;
	}
}
