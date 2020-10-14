import pygame,sys,random

def draw_floor():
    screen.blit(floor_surface, (floor_x_pos, 452))
    screen.blit(floor_surface, (floor_x_pos+288, 452))


def create_pipe():
    random_pipe_height = random.choice(pipe_height)
    random_gap = random.choice([100,150,90,125,175])
    bottom_pipe_rect = pipe_surface.get_rect(midtop = (295,random_pipe_height))
    top_pipe_rect = pipe_surface.get_rect(midbottom=(295, random_pipe_height-random_gap))
    return bottom_pipe_rect,top_pipe_rect


def move_pipes(pipes):
    for pipe in pipes:
        pipe.centerx -= 2
    return pipes


def draw_pipes(pipes):
    for pipe_rect in pipes:
        if pipe_rect.bottom >= 512 :
            screen.blit(pipe_surface,pipe_rect)
        else:
            flip_pipe = pygame.transform.flip(pipe_surface,False,True)
            screen.blit(flip_pipe,pipe_rect)


def check_collision(pipes):
    for pipe_rect in pipes:
        if bird_rect.colliderect(pipe_rect):
            # print("collision")
            return False
    if bird_rect.top <= -10 or bird_rect.bottom >= 452:
        # print("collision")
        return False
    return True


def rotate_bird(bird):
    new_bird = pygame.transform.rotozoom(bird,-bird_movement*3,1)
    return new_bird


def bird_animation():
    new_bird = bird_frames[bird_index]
    new_bird_rect = new_bird.get_rect(center=(50, bird_rect.centery))
    return new_bird,new_bird_rect


def score_display(game_state):
    if game_state == 'main_game':
        score_surface = game_font.render(str(int(score)),True,(255,255,255))
        score_rect = score_surface.get_rect(center=(144,60))
        screen.blit(score_surface,score_rect)
    if game_state == 'game_over':
        score_surface = game_font.render(f'Score : {int(score)}',True,(255,255,255))
        score_rect = score_surface.get_rect(center=(144,60))
        screen.blit(score_surface,score_rect)

        highscore_surface = game_font.render(f'High Score : {int(high_score)}',True,(255,255,255))
        highscore_rect = highscore_surface.get_rect(center=(144,420))
        screen.blit(highscore_surface,highscore_rect)


def update_score(score, high_score):
    if score > high_score:
        high_score = score
    return high_score


pygame.init()

# game variable
gravity = 0.15
bird_movement = 0
Game_active = True
score = 0
high_score = 0

screen = pygame.display.set_mode(size=(288,512))
clock = pygame.time.Clock()
game_font = pygame.font.Font('04B_19__.TTF',30)

bg_surface = pygame.image.load('background-day.png').convert()

floor_surface = pygame.image.load('base.png').convert()
floor_x_pos = 0

# bird_surface = pygame.image.load('bluebird-midflap.png').convert_alpha()
# bird_rect = bird_surface.get_rect(center=(50, 256))

bird_downflap = pygame.image.load('bluebird-downflap.png').convert_alpha()
bird_midflap = pygame.image.load('bluebird-midflap.png').convert_alpha()
bird_upflap = pygame.image.load('bluebird-upflap.png').convert_alpha()
bird_frames = [bird_downflap,bird_midflap,bird_upflap]
bird_index = 0
bird_surface = bird_frames[bird_index]
bird_rect = bird_surface.get_rect(center=(50, 256))

BIRDFLAP = pygame.USEREVENT + 1
pygame.time.set_timer(BIRDFLAP,200)

pipe_surface = pygame.image.load('pipe-green.png').convert()
pipe_list_rect = []
pipe_height = [250,300,400]
SPAWNPIPE = pygame.USEREVENT
pygame.time.set_timer(SPAWNPIPE, 1200)


game_over_surface = pygame.image.load('message.png').convert_alpha()
game_over_rect = game_over_surface.get_rect(center=(144,256))

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_SPACE and Game_active:
                bird_movement = 0
                bird_movement -= 3
            if event.key == pygame.K_SPACE and Game_active == False:
                Game_active = True
                bird_rect.center = (50,256)
                pipe_list_rect.clear()
                bird_movement = 0
                score = 0
        if event.type == SPAWNPIPE:
            pipe_list_rect.extend(create_pipe())
        if event.type == BIRDFLAP:
            if bird_index < 2:
                bird_index += 1
            else:
                bird_index = 0
            bird_surface,bird_rect = bird_animation()

    screen.blit(bg_surface,(0,0))

    if Game_active:
        bird_movement += gravity
        bird_rect.centery += bird_movement
        rotated_bird = rotate_bird(bird_surface)
        screen.blit(rotated_bird,bird_rect)

        pipe_list1 = move_pipes(pipe_list_rect)
        draw_pipes(pipe_list1)
        Game_active = check_collision(pipe_list_rect)

        score += 0.01
        score_display('main_game')
    else:
        screen.blit(game_over_surface,game_over_rect)
        high_score = update_score(score, high_score)
        score_display('game_over')

    floor_x_pos -= 1
    draw_floor()
    if floor_x_pos <= -288:
        floor_x_pos = 0

    pygame.display.update()
    clock.tick(120)