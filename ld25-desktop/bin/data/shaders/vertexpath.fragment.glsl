//#define lightmapTextureFlag
//#define diffuseTextureFlag
//#define translucentFlag
//#define fogColorFlag

#ifdef GL_ES
#define LOWP lowp
#define MED mediump
precision lowp float;
#else
#define MED
#define LOWP
#endif

#ifdef diffuseTextureFlag
uniform sampler2D diffuseTexture;
#endif

#ifdef lightmapTextureFlag
uniform sampler2D lightmapTexture;
#endif

#ifdef fogColorFlag
uniform vec4 fogColor;
varying float v_fog;
#endif

#ifdef envmapFlag
varying vec3 v_reflect;
uniform float reflectivity;
uniform sampler2D envmap;
#endif

varying vec2 v_texCoords;
varying vec4 v_diffuse;
void main()
{		
	vec4 light = v_diffuse;
	
	#ifdef lightmapTextureFlag
	light *= texture2D(lightmapTexture, v_texCoords);
	#endif
	
	#ifdef diffuseTextureFlag
	vec4 diffColor = texture2D(diffuseTexture, v_texCoords);
	light.rgb *= diffColor.rgb;
	light.a = diffColor.a;
	#endif
	
	#ifdef fogColorFlag
    float z = gl_FragCoord.z / gl_FragCoord.w;
	float fogFactor = exp2(-0.08 * 0.08 * z * 0.5);
	fogFactor = clamp(fogFactor,0.0,1.0);
	light.rgb = mix(fogColor.rgb, light.rgb, fogFactor);		
		#ifdef translucentFlag
			light.a += v_fog;
		#endif
	#endif
	
	#ifdef envmapFlag
        vec4 envmapColor = texture2D( envmap, v_reflect.xy * 0.5 + 0.5);
        light = mix( light, envmapColor, 0.2 );
    #endif	

	gl_FragColor.rgb = light.rgb;
	#ifdef translucentFlag
	gl_FragColor.a = light.a;
	#endif
}
