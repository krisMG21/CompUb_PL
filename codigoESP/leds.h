struct leds {
    int red;
    int green;
    int pomodoro[6];
};

extern struct leds leds;

void leds_init(int red, int green, int pomodoro[6]);
void change_occupied();
void update_leds();


