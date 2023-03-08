



const tl = gsap.timeline({ defaults: { duration: 1, ease: "power1.out" } });

gsap.fromTo('.cook', { opacity: 0, y: -100 }, { opacity: 1, y: 0, duration: 1 })
tl.fromTo('.slider', { x: -1000, opacity: 0 }, { x: 0, opacity: 1,ease: "power1.out"  });
gsap.fromTo('.food', { opacity: 0, y: -50 }, { opacity: 1, y: 0, duration: 1})

tl.fromTo('.left', { opacity: 0, x: 30 }, { opacity: 1, x: 0 });
gsap.fromTo('.burger', {opacity:0, y:70}, {opacity:1, y:0 , duration: 1 , ease : "power1.out"})

// Menu bar sliding animation

/*gsap.fromTo(".menubar" ,{x : -1000, opacity:0}, {x:0,opacity:1,duration:0.5, ease:"power1.out"});
gsap.fromTo(".logoutbutton" ,{x : -1000, opacity:0}, {x:0,opacity:1,duration:0.5, ease:"power1.out"});*/



console.log("Inside Style")





