package ir.apache.lucene;

/**
 * @author Ranjan Venkatesh
 *
 */
public class SearchResultData {
private String url;
private String imageUrl;
private String title;
private String summary;

public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getImageUrl() {
	return imageUrl;
}

public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getSummary() {
	return summary;
}
public void setSummary(String summary) {
	this.summary = summary;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
	result = prime * result + ((summary == null) ? 0 : summary.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	SearchResultData other = (SearchResultData) obj;
	if (imageUrl == null) {
		if (other.imageUrl != null)
			return false;
	} else if (!imageUrl.equals(other.imageUrl))
		return false;
	if (summary == null) {
		if (other.summary != null)
			return false;
	} else if (!summary.equals(other.summary))
		return false;
	if (title == null) {
		if (other.title != null)
			return false;
	} else if (!title.equals(other.title))
		return false;
	if (url == null) {
		if (other.url != null)
			return false;
	} else if (!url.equals(other.url))
		return false;
	return true;
}

}
