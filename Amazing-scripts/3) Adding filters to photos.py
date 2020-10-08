from PIL import Image, ImageFilter

from PIL.ImageFilter import (
   BLUR, CONTOUR, DETAIL, EDGE_ENHANCE, EDGE_ENHANCE_MORE,
   EMBOSS, FIND_EDGES, SMOOTH, SMOOTH_MORE, SHARPEN
)

def filter(image,filter):
    new_image=image.filter(filter)
    new_image.save('images/new_image.jpg')
    new_image.show()


file='example.jpg'
image=Image.open(f'{file}')
Filters=[BLUR, CONTOUR, DETAIL, EDGE_ENHANCE, EDGE_ENHANCE_MORE,EMBOSS, FIND_EDGES, SMOOTH, SMOOTH_MORE, SHARPEN]
filter(image,Filters[0])
