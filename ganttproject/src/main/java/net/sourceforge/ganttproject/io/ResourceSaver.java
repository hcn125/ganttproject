/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2011 GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.sourceforge.ganttproject.io;

import net.sourceforge.ganttproject.CustomProperty;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.sax.TransformerHandler;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

class ResourceSaver extends SaverBase {
  void save(IGanttProject project, TransformerHandler handler) throws SAXException {
    final AttributesImpl attrs = new AttributesImpl();
    startElement("resources", handler);
    saveCustomColumnDefinitions(project, handler);

    for (HumanResource p : project.getHumanResourceManager().getResources()) {
      addAttribute("id", p.getId(), attrs);
      addAttribute("name", p.getName(), attrs);
      addAttribute("function", p.getRole().getPersistentID(), attrs);
      addAttribute("contacts", p.getMail(), attrs);
      addAttribute("phone", p.getPhone(), attrs);
      startElement("resource", attrs, handler);
      {
        saveRates(p, handler);
        saveCustomProperties(project, p, handler);
      }
      endElement("resource", handler);
    }
    endElement("resources", handler);
  }

  private void saveRates(HumanResource p, TransformerHandler handler) throws SAXException {
    if (!BigDecimal.ZERO.equals(p.getStandardPayRate())) {
      AttributesImpl attrs = new AttributesImpl();
      addAttribute("name", "standard", attrs);
      addAttribute("value", p.getStandardPayRate().toPlainString(), attrs);
      emptyElement("rate", attrs, handler);
    }
  }

  private void saveCustomProperties(IGanttProject project, HumanResource resource, TransformerHandler handler)
      throws SAXException {
    AttributesImpl attrs = new AttributesImpl();
    List<CustomProperty> properties = resource.getCustomProperties();
    for (int i = 0; i < properties.size(); i++) {
      CustomProperty property = properties.get(i);
      CustomPropertyDefinition propertyDefinition = property.getDefinition();

      assert propertyDefinition != null : "WTF? null property definition for property=" + i + "(value="
          + property.getValueAsString() + ")";

      if (property.getValue() != null && !property.getValue().equals(propertyDefinition.getDefaultValue())) {
        addAttribute("definition-id", propertyDefinition.getID(), attrs);
        addAttribute("value", property.getValueAsString(), attrs);
        emptyElement("custom-property", attrs, handler);
      }
    }
  }

  private void saveCustomColumnDefinitions(IGanttProject project, TransformerHandler handler) throws SAXException {
    CustomPropertyManager customPropsManager = project.getHumanResourceManager().getCustomPropertyManager();
    List<CustomPropertyDefinition> definitions = customPropsManager.getDefinitions();
    final AttributesImpl attrs = new AttributesImpl();
    for (CustomPropertyDefinition nextDefinition : definitions) {
      addAttribute("id", nextDefinition.getID(), attrs);
      addAttribute("name", nextDefinition.getName(), attrs);
      addAttribute("type", nextDefinition.getTypeAsString(), attrs);
      addAttribute("default-value", nextDefinition.getDefaultValueAsString(), attrs);
      for (Map.Entry<String, String> kv : nextDefinition.getAttributes().entrySet()) {
        addAttribute(kv.getKey(), kv.getValue(), attrs);
      }
      emptyElement("custom-property-definition", attrs, handler);
    }
  }
}
