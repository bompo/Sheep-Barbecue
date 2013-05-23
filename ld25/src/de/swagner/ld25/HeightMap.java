package de.swagner.ld25;

import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.model.still.StillSubMesh;

public class HeightMap {
	
	public float[] map; 

	public HeightMap(StillModel plane) {
		//iterate through model and generate grid from vertices height
		int len = plane.subMeshes.length;
		for (int i = 0; i < len; i++) {
			StillSubMesh subMesh = plane.subMeshes[i];
			
			map = new float[subMesh.mesh.getNumVertices()*3];
			
			int j = 0;
			for(int n=0; n < subMesh.mesh.getNumVertices()*8; n=n+8) {
				//y is height
				float x = subMesh.mesh.getVerticesBuffer().get(n);
				float y = subMesh.mesh.getVerticesBuffer().get(n+1);
				float z = subMesh.mesh.getVerticesBuffer().get(n+2);
				
				map[j] = x;
				map[j + 1] = y;
				map[j + 2] = z;
				
				j = j + 3; 
			}
			
		}
			
		
	}
	
}
