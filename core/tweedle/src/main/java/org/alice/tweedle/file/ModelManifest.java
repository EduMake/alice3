package org.alice.tweedle.file;

import org.lgna.project.annotations.Visibility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelManifest extends Manifest {

	public String parentClass;
	public List<String> rootJoints =  new ArrayList<>();
	public List<Joint> additionalJoints =  new ArrayList<>();
	public List<JointArray> additionalJointArrays = new ArrayList<>();
	public List<JointArrayId> additionalJointArrayIds = new ArrayList<>();
	public List<Pose> poses =  new ArrayList<>();
	public BoundingBox boundingBox;
	public Boolean placeOnGround = true;
	public List<TextureSet> textureSets = new ArrayList<>();
	public List<StructureReference> structures =  new ArrayList<>();
	public List<ModelVariant> models =  new ArrayList<>();


	public static class BoundingBox
	{
		public List<Float> min;
		public List<Float> max;
	}

	public static class ModelVariant {
		public String id;
		public String structure;
		public String textureSet;
		public String icon;
	}

	public static class TextureSet {
		public String id;
		public Map<Integer, String> idToResourceMap = new HashMap<>();
	}

	public static class Joint {
		public String name;
		public String parent;
		public Visibility visibility;
	}

	public static class JointArray {
		public String name;
		public Visibility visibility;
		public List<String> jointIds = new ArrayList<>();
	}

	public static class JointArrayId {
		public String name;
		public Visibility visibility;
		public String patternId;
		public String rootJoint;
	}

	public static class Pose {
		public String name;
		public List<JointTransform> transforms = new ArrayList<>();
	}

	public static class JointTransform {
		public String jointName;
		public List<Float> orientation;
		public List<Float> position;
	}

	public ModelVariant getModelVariant(String variantId) {
		for (ModelVariant variant : models) {
			if (variant.id.equals(variantId)) {
				return variant;
			}
		}
		return null;
	}

	public StructureReference getStructure(String structureId) {
		for (StructureReference structure : structures) {
			if (structure.id.equals(structureId)) {
				return structure;
			}
		}
		return null;
	}

	public TextureSet getTextureSet(String textureSetId) {
		for (TextureSet textureSet : textureSets) {
			if (textureSet.id.equals(textureSetId)) {
				return textureSet;
			}
		}
		return null;
	}
}
