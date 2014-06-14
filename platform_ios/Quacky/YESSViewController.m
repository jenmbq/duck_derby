//
//  YESSViewController.m
//  Quacky
//
//  Created by Song Zhang on 6/12/14.
//  Copyright (c) 2014 YESS. All rights reserved.
//

#import "YESSViewController.h"

#define kDuckFeetWalkingAnimationDuration   4.0

#define kSegueToWebViewIdentifier   @"IntroToWebView"
#define kSegueToWebViewDelay        3.0

@interface YESSViewController ()

@property (nonatomic, weak) IBOutlet UIImageView *feetImageView;

@end

@implementation YESSViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self.view setBackgroundColor:kYESSBlueColour];
    
    [self performSelector:@selector(performSegueWithIdentifier:sender:) withObject:kSegueToWebViewIdentifier afterDelay:2.0];
    
//    _feetImageView.animationImages = [NSArray arrayWithObjects:
//                                      [UIImage imageNamed:@"DuckFootRight"],
//                                      [UIImage imageNamed:@"DuckFootLeft"],
//                                      nil];
//    _feetImageView.animationRepeatCount = 100;
//    _feetImageView.animationDuration = 0.4;
//    _feetImageView.hidden = YES;
}

- (void) viewDidAppear:(BOOL)animated
{
//    [super viewDidAppear:animated];
//    
//    _feetImageView.hidden = NO;
//    _feetImageView.center = self.view.center;
//    _feetImageView.frame = CGRectMake(_feetImageView.frame.origin.x, self.view.frame.size.height, _feetImageView.frame.size.width, _feetImageView.frame.size.height);
//    
//    [_feetImageView startAnimating];
//    
//    [UIView animateWithDuration:kDuckFeetWalkingAnimationDuration delay:0.5 options:UIViewAnimationOptionCurveEaseIn animations:^{
//        _feetImageView.frame = CGRectMake(_feetImageView.frame.origin.x, -_feetImageView.frame.size.height, _feetImageView.frame.size.width, _feetImageView.frame.size.height);
//    } completion:nil];
    
}

@end
