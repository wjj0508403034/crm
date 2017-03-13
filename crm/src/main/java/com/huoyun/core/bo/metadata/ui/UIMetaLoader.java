package com.huoyun.core.bo.metadata.ui;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.huoyun.core.bo.metadata.ui.elements.RootElement;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.exception.BusinessException;

public class UIMetaLoader {
	private final static String Location = "classpath:/ui-meta/*.xml";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UIMetaLoader.class);
	private final Map<String, RootElement> cache = new LinkedHashMap<>();

	private static UIMetaLoader instance;

	private UIMetaLoader() throws BusinessException {
		this.load();
	}

	public static synchronized UIMetaLoader getInstance()
			throws BusinessException {
		if (instance == null) {
			instance = new UIMetaLoader();
		}
		return instance;
	}

	public RootElement getUIMetaElement(String boNamespace, String boName) {
		String key = BusinessObjectUtils.getFullName(boNamespace, boName);
		if (this.cache.containsKey(key)) {
			return this.cache.get(key);
		}

		return null;
	}

	private void load() throws BusinessException {
		LOGGER.info("================================================================");
		LOGGER.info("= Start loading UI meta xml ...");
		LOGGER.info("================================================================");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
				UIMetaLoader.class.getClassLoader());
		Resource currentSrc = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(RootElement.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			for (Resource resource : resolver.getResources(Location)) {
				currentSrc = resource;
				LOGGER.info("parse ui meta file:{}", resource.getFilename());
				URL url = resource.getURL();
				RootElement root = (RootElement) unmarshaller.unmarshal(url);
				this.cache.put(BusinessObjectUtils.getFullName(
						root.getNamespace(), root.getName()), root);
			}

		} catch (Exception e) {
			String filePath = currentSrc != null ? currentSrc.getFilename()
					: Location;
			LOGGER.error("UI metadata loading error with file:" + filePath, e);

			throw new BusinessException(ErrorCode.LoadUIXMLFailed);
		}

		LOGGER.info("================================================================");
		LOGGER.info("= End load UI meta xml.");
		LOGGER.info("================================================================");
	}
}
