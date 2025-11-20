"use client";

import { useEffect } from "react";
import StorePage from "../page";

export default function CartPage() {
  // Quando alguém acessa /cart, redirecionamos para a loja com o cart aberto
  useEffect(() => {
    const evt = new CustomEvent("open-cart");
    window.dispatchEvent(evt);
  }, []);

  return <StorePage />;
}
