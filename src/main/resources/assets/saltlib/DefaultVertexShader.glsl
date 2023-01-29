#version 330 core

layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in vec4 aColor;
layout(location = 3) in vec3 aNormal;

out vec2 vTexCoord;
out vec4 vColor;
out vec3 vNormal;

void main()
{
    vTexCoord = aTexCoord;
    vColor = aColor;
    vNormal = aNormal;
    
    gl_Position = vec4(aPosition, 1.0);
}