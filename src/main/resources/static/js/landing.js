(() => {
  "use strict";

  const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  /* ------------------------- Sticky nav background ------------------------ */
  const nav = document.querySelector(".site-nav");
  const navCollapse = document.getElementById("mainNav");
  const navLinks = Array.from(document.querySelectorAll('.site-nav .nav-link[href^="#"]'));
  const sections = Array.from(document.querySelectorAll("main section[id]"));

  const onScroll = () => {
    if (nav) {
      nav.classList.toggle("is-scrolled", window.scrollY > 8);
    }

    if (sections.length === 0) return;
    const offset = (nav ? nav.offsetHeight : 0) + 48;
    let currentId = sections[0].id;

    for (const section of sections) {
      if (section.getBoundingClientRect().top - offset <= 0) {
        currentId = section.id;
      }
    }

    navLinks.forEach((link) => {
      const isActive = link.getAttribute("href") === `#${currentId}`;
      link.classList.toggle("active", isActive);
      if (isActive) {
        link.setAttribute("aria-current", "true");
      } else {
        link.removeAttribute("aria-current");
      }
    });
  };

  let ticking = false;
  window.addEventListener(
    "scroll",
    () => {
      if (ticking) return;
      ticking = true;
      window.requestAnimationFrame(() => {
        onScroll();
        ticking = false;
      });
    },
    { passive: true }
  );
  window.addEventListener("resize", onScroll, { passive: true });
  onScroll();

  /* --------------------------- Reveal on scroll --------------------------- */
  const revealItems = document.querySelectorAll(".reveal");

  if (reduceMotion || !("IntersectionObserver" in window)) {
    revealItems.forEach((item) => item.classList.add("is-visible"));
  } else {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (!entry.isIntersecting) return;
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        });
      },
      { rootMargin: "0px 0px -8% 0px", threshold: 0.12 }
    );

    revealItems.forEach((item) => observer.observe(item));
  }

  /* ------------------- Close mobile menu after navigation ---------------- */
  if (navCollapse) {
    navCollapse.querySelectorAll("a[href^='#']").forEach((link) => {
      link.addEventListener("click", () => {
        if (!navCollapse.classList.contains("show")) return;
        const Collapse = window.bootstrap?.Collapse;
        if (Collapse) {
          Collapse.getOrCreateInstance(navCollapse).hide();
        } else {
          navCollapse.classList.remove("show");
        }
      });
    });
  }
})();
